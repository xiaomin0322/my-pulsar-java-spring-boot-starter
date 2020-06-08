package io.github.majusko.pulsar.collector;

import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.constant.Serialization;

public class ProducerHolder {

	/**
	 * 
	 */
	private String topic;
	private Class<?> clazz;
	private Serialization serialization;
	private ProducerConfigurationDataExt configurationDataExt;

	public ProducerHolder() {
	}

	public ProducerHolder(String topic, Class<?> clazz) {
		this.topic = topic;
		this.clazz = clazz;
	}

	public ProducerHolder(String topic, Class<?> clazz, Serialization serialization) {
		this.topic = topic;
		this.clazz = clazz;
		this.serialization = serialization;
	}

	public String getTopic() {
		return topic;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Serialization getSerialization() {
		return serialization;
	}

	public ProducerConfigurationDataExt getConfigurationDataExt() {
		return configurationDataExt;
	}

	public void setConfigurationDataExt(ProducerConfigurationDataExt configurationDataExt) {
		this.configurationDataExt = configurationDataExt;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public void setSerialization(Serialization serialization) {
		this.serialization = serialization;
	}

}
