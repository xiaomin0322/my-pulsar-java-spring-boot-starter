package io.github.majusko.pulsar.producer;

import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.constant.Serialization;

public class ProducerHolder {

	public void setConfig(ProducerConfigurationDataExt config) {
		this.config = config;
	}

	private String topic;

	private Class<?> clazz;

	private Serialization serialization = Serialization.JSON;

	private ProducerConfigurationDataExt config;

	public Serialization getSerialization() {
		return serialization;
	}

	public void setSerialization(Serialization serialization) {
		this.serialization = serialization;
	}

	public ProducerHolder() {
	}

	public ProducerConfigurationDataExt getDef() {
		config = new ProducerConfigurationDataExt();
		config.setTopicName(topic);
		return config;
	}

	public ProducerConfigurationDataExt getConfig() {
		return getDef();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
