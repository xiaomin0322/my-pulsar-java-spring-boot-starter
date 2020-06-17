package io.github.majusko.pulsar.collector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.config.ConsumerCustomConfig;
import io.github.majusko.pulsar.config.ConsumerCustomDetailConfig;
import io.github.majusko.pulsar.consumer.ConsumerHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsumerCollector implements BeanPostProcessor, CommandLineRunner {

	private Map<String, ConsumerHolder> consumers = new ConcurrentHashMap<>();

	@Autowired
	private ConsumerCustomConfig consumerCustomConfig;

	/**
	 * 加载注解配置
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		final Class<?> beanClass = bean.getClass();

		consumers.putAll(Arrays.stream(beanClass.getDeclaredMethods())
				.filter($ -> $.isAnnotationPresent(PulsarConsumer.class))
				.collect(Collectors.toMap(method -> beanClass.getName() + "#" + method.getName(),
						method -> new ConsumerHolder(method.getAnnotation(PulsarConsumer.class), method, bean))));
		return bean;
	}

	public static String methodSign(String className, Method method) {
		final StringBuilder builder = new StringBuilder();
		if (method == null) {
			return builder.toString();
		}
		builder.append(className).append("#").append(method.getName());
		Class<?>[] typeParameters = method.getParameterTypes();
		if (ArrayUtils.isEmpty(typeParameters)) {
			return builder.toString();
		}
		builder.append("(");

		Arrays.stream(typeParameters).forEach($ -> builder.append($.getName()).append(","));

		builder.replace(builder.length() - 1, builder.length(), ")");
		// System.out.println(className + " name :" + builder.toString());

		return builder.toString();
	}

	/**
	 * 加载yml配置
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if (CollectionUtils.isEmpty(consumerCustomConfig.getConsumer())) {
			return bean;
		}
		Collection<ConsumerCustomDetailConfig> consumerCustomDetailConfigs = consumerCustomConfig.getConsumer()
				.values();
		if (CollectionUtils.isEmpty(consumerCustomDetailConfigs)) {
			return bean;
		}
		final Class<?> beanClass = bean.getClass();
		consumers.putAll(Arrays.stream(beanClass.getDeclaredMethods()).filter($ -> {
			Optional<ConsumerCustomDetailConfig> optional = consumerCustomDetailConfigs.stream()
					.filter(c -> methodSign(beanName, $).equals(c.getMethodSign())).findFirst();
			return optional.isPresent();
		}).collect(
				Collectors.toMap(method -> beanClass.getName() + "#" + method.getName(),
						method -> new ConsumerHolder(consumerCustomDetailConfigs.stream()
								.filter(c -> methodSign(beanName, method).equals(c.getMethodSign())).findFirst().get(),
								method, bean))));

		return bean;
	}

	public Map<String, ConsumerHolder> getConsumers() {
		return consumers;
	}

	public Optional<ConsumerHolder> getConsumer(String methodDescriptor) {
		return Optional.ofNullable(consumers.get(methodDescriptor));
	}

	@Override
	public void run(String... args) throws Exception {

		log.info("consumers topic keys {}", consumers.keySet());
	}
}
