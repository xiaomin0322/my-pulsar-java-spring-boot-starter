package io.github.majusko.pulsar.producer;

import io.github.majusko.pulsar.config.ProducerCustomDetailConfig;

public class ProducerHolder extends ProducerCustomDetailConfig {

	public static ProducerCustomDetailConfig getDef(String topic) {
		ProducerCustomDetailConfig producerCustomDetailConfig = new ProducerCustomDetailConfig();
		producerCustomDetailConfig.setTopic(topic);
		return producerCustomDetailConfig;
	}
}
