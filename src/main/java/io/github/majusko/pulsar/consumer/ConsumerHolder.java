package io.github.majusko.pulsar.consumer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;
import io.github.majusko.pulsar.config.ConsumerCustomDetailConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerHolder extends ConsumerCustomDetailConfig {

	private PulsarConsumer annotation;
	private Method handler;
	private Object bean;

	public ConsumerHolder(PulsarConsumer annotation, Method handler, Object bean) {
		this.annotation = annotation;
		this.handler = handler;
		this.bean = bean;
		if (annotation != null) {
			super.setTopic(annotation.topic());
			super.setSerialization(annotation.serialization());
			super.setClazz(annotation.clazz());
		}

	}

	public ConsumerHolder(ConsumerCustomDetailConfig config, Method handler, Object bean) {
		this.handler = handler;
		this.bean = bean;
		BeanUtils.copyProperties(config, this);
	}

	public ConsumerConfigurationDataExt getDefConfig() {
		ConsumerConfigurationDataExt configurationDataExt = new ConsumerConfigurationDataExt();
		String name = bean.getClass().getSimpleName() + "#" + handler.getName();
		configurationDataExt.setSubscriptionType(annotation.subscriptionType());
		configurationDataExt.setConsumerName("consumer-" + name);
		configurationDataExt.setSubscriptionName("subscription-" + name);
		configurationDataExt.setTopic(annotation.topic());
		return configurationDataExt;
	}

	public ConsumerConfigurationDataExt getConfig() {
		ConsumerConfigurationDataExt configurationDataExt = super.getConfig();
		// 优先加载yml种配置
		if (configurationDataExt != null) {
			return configurationDataExt;
		}
		// 注解配置
		configurationDataExt = builderConfig(annotation);
		if (configurationDataExt != null) {
			return configurationDataExt;
		}
		// 默认配置
		configurationDataExt = getDefConfig();
		return configurationDataExt;
	}

	public ConsumerConfigurationDataExt builderConfig(PulsarConsumer annotation) {
		if (annotation == null || ArrayUtils.isEmpty(annotation.configuration())) {
			return null;
		}
		try {
			ConsumerConfigurationDataExt def = getDefConfig();
			// 注解配置
			Class<?> clazz = annotation.configuration()[0];
			Optional<Method> findFirst = Arrays.stream(clazz.getDeclaredMethods())
					.filter($ -> $.getReturnType().equals(ConsumerConfigurationDataExt.class)).findFirst();
			if (!findFirst.isPresent()) {
				return null;
			}
			Object newInstance = clazz.newInstance();
			ConsumerConfigurationDataExt configurationDataExt = (ConsumerConfigurationDataExt) findFirst.get()
					.invoke(newInstance);
			if (StringUtils.isBlank(configurationDataExt.getTopic())) {
				configurationDataExt.setTopic(def.getTopic());
			}
			if (StringUtils.isBlank(configurationDataExt.getConsumerName())) {
				configurationDataExt.setConsumerName(def.getConsumerName());
			}
			if (StringUtils.isBlank(configurationDataExt.getSubscriptionName())) {
				configurationDataExt.setSubscriptionName(def.getSubscriptionName());
			}
			if (configurationDataExt.getSubscriptionType() == null) {
				configurationDataExt.setSubscriptionType(def.getSubscriptionType());
			}
			return configurationDataExt;
		} catch (Exception e) {
			log.error("getConfigException", e);
		}
		return null;
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
