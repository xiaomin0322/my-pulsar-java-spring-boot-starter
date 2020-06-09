package io.github.majusko.pulsar.consumer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Sets;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;

public class ConsumerHolder {

	private final PulsarConsumer annotation;
	private final Method handler;
	private final Object bean;

	public ConsumerHolder(PulsarConsumer annotation, Method handler, Object bean) {
		this.annotation = annotation;
		this.handler = handler;
		this.bean = bean;
	}

	@SuppressWarnings("unchecked")
	public ConsumerConfigurationDataExt getDef() {
		ConsumerConfigurationDataExt configurationDataExt = new ConsumerConfigurationDataExt();
		String name = bean.getClass().getSimpleName() + "#" + handler.getName();
		configurationDataExt.setSubscriptionType(annotation.subscriptionType());
		configurationDataExt.setConsumerName("consumer-" + name);
		configurationDataExt.setSubscriptionName("subscription-" + name);
		configurationDataExt.setTopicNames(Sets.newHashSet(annotation.topic()));
		return configurationDataExt;
	}

	public ConsumerConfigurationDataExt getConsumerConfigurationDataExt() {
		ConsumerConfigurationDataExt def = getDef();
		if (annotation == null || ArrayUtils.isEmpty(annotation.configuration())) {
			return def;
		}
		try {
			Class<?> clazz = annotation.configuration()[0];
			Optional<Method> findFirst = Arrays.stream(clazz.getDeclaredMethods())
					.filter($ -> $.getReturnType().equals(ConsumerConfigurationDataExt.class)).findFirst();
			if (!findFirst.isPresent()) {
				return null;
			}
			Object newInstance = clazz.newInstance();
			ConsumerConfigurationDataExt configurationDataExt = (ConsumerConfigurationDataExt) findFirst.get()
					.invoke(newInstance);
			return configurationDataExt;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return def;
	}

	public PulsarConsumer getAnnotation() {
		return annotation;
	}

	public Method getHandler() {
		return handler;
	}

	public Object getBean() {
		return bean;
	}
}
