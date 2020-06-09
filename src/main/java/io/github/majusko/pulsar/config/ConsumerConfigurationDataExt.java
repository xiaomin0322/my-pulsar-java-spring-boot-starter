package io.github.majusko.pulsar.config;

import org.apache.pulsar.client.impl.conf.ConsumerConfigurationData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;

@SuppressWarnings("rawtypes")
public class ConsumerConfigurationDataExt extends ConsumerConfigurationData {

	private String topic;

	@SuppressWarnings("unchecked")
	@JsonIgnore
	public void setTopic(String topic) {
		this.topic = topic;
		setTopicNames(Sets.newHashSet(topic));
	}

	public String getTopic() {
		return topic;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
