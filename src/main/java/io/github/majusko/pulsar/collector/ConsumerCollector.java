package io.github.majusko.pulsar.collector;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;
import io.github.majusko.pulsar.config.ConsumerCustomConfig;
import io.github.majusko.pulsar.config.ConsumerCustomDetailConfig;
import io.github.majusko.pulsar.consumer.ConsumerHolder;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ConsumerCollector implements BeanPostProcessor, CommandLineRunner {

	private Map<String, ConsumerHolder> consumers = new ConcurrentHashMap<>();

	private Map<String, ConsumerConfigurationDataExt> consumerCustomDetailConfigMap = new ConcurrentHashMap<>();

	@Autowired
	private ConsumerCustomConfig consumerCustomConfig;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		final Class<?> beanClass = bean.getClass();

		consumers.putAll(Arrays.stream(beanClass.getDeclaredMethods())
				.filter($ -> $.isAnnotationPresent(PulsarConsumer.class))
				.collect(Collectors.toMap(method -> beanClass.getName() + "#" + method.getName(),
						method -> new ConsumerHolder(method.getAnnotation(PulsarConsumer.class), method, bean))));
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	public Map<String, ConsumerHolder> getConsumers() {
		return consumers;
	}
	
	

	public Map<String, ConsumerConfigurationDataExt> getConsumerCustomDetailConfigMap() {
		return consumerCustomDetailConfigMap;
	}

	public void setConsumerCustomDetailConfigMap(Map<String, ConsumerConfigurationDataExt> consumerCustomDetailConfigMap) {
		this.consumerCustomDetailConfigMap = consumerCustomDetailConfigMap;
	}

	public Optional<ConsumerHolder> getConsumer(String methodDescriptor) {
		return Optional.ofNullable(consumers.get(methodDescriptor));
	}

	@Override
	public void run(String... args) throws Exception {

		//java定义得配置
		consumerCustomDetailConfigMap.putAll(consumers.values().stream()
				.collect(Collectors.toMap(ConsumerHolder::getTopic, ConsumerHolder::getConfig)));

		//配置文件的配置
		Map<String, ConsumerCustomDetailConfig> consumersMap = consumerCustomConfig.getConsumer();
		if (!CollectionUtils.isEmpty(consumersMap)) {
			consumerCustomDetailConfigMap.putAll(consumersMap.values().stream().collect(
					Collectors.toMap(ConsumerCustomDetailConfig::getTopic, ConsumerCustomDetailConfig::getConfig)));
		}
		log.info("consumers topic keys {}", consumers.keySet());
	}
}
