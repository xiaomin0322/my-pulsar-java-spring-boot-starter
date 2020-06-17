package io.github.majusko.pulsar.producer;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.config.ProducerCustomDetailConfig;
import io.github.majusko.pulsar.constant.Constants;

public class ProducerHolder extends ProducerCustomDetailConfig {

	public static <T> ProducerCustomDetailConfig getDefConfig(String topic, T t,
			ProducerCustomDetailConfig producerCustomDetailConfig) {
		if (producerCustomDetailConfig == null) {
			producerCustomDetailConfig = new ProducerCustomDetailConfig();
		}
		producerCustomDetailConfig.setTopic(topic);
		producerCustomDetailConfig.setClazz(t.getClass());
		return producerCustomDetailConfig;
	}

	public static <T> ProducerCustomDetailConfig getDefConfig(String topic, T t,
			Map<String, ProducerCustomDetailConfig> configMap) {
		ProducerCustomDetailConfig producerCustomDetailConfig = null;
		if (!CollectionUtils.isEmpty(configMap)) {
			producerCustomDetailConfig = configMap.get(Constants.DEF_PROD_CONF_KEY);
		}
		producerCustomDetailConfig = getDefConfig(topic, t, producerCustomDetailConfig);
		return producerCustomDetailConfig;
	}
}
