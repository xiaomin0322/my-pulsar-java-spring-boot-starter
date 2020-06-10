package io.github.majusko.pulsar.config;

import org.apache.pulsar.client.impl.conf.ProducerConfigurationData;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProducerConfigurationDataExt extends ProducerConfigurationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
