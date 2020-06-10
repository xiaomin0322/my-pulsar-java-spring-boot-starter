package io.github.majusko.pulsar.config;

import org.apache.pulsar.client.impl.conf.ConsumerConfigurationData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;

@SuppressWarnings("rawtypes")
public class ConsumerConfigurationDataExt extends ConsumerConfigurationData {

	@JsonIgnore
	private String topic;


	
	public String getTopic() {
		return topic;
	}

	@SuppressWarnings("unchecked")
	public void setTopic(String topic) {
		this.topic = topic;
		super.setTopicNames(Sets.newHashSet(topic));
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
