package io.github.majusko.pulsar.collector;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.consumer.ConsumerHolder;

@Configuration
public class ConsumerCollector implements BeanPostProcessor {

	private Map<String, ConsumerHolder> consumers = new ConcurrentHashMap<>();

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

	public Optional<ConsumerHolder> getConsumer(String methodDescriptor) {
		return Optional.ofNullable(consumers.get(methodDescriptor));
	}
}
