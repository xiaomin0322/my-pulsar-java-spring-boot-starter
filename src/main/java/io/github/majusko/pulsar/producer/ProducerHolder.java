package io.github.majusko.pulsar.producer;

import io.github.majusko.pulsar.config.ProducerCustomDetailConfig;

public class ProducerHolder extends ProducerCustomDetailConfig {

	public static <T> ProducerCustomDetailConfig getDefConfig(String topic,T t) {
		ProducerCustomDetailConfig producerCustomDetailConfig = new ProducerCustomDetailConfig();
		producerCustomDetailConfig.setTopic(topic);
		producerCustomDetailConfig.setClazz(t.getClass());
		return producerCustomDetailConfig;
	}
}
