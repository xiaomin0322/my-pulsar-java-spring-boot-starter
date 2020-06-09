package io.github.majusko.pulsar.config;

import org.apache.pulsar.client.impl.conf.ConsumerConfigurationData;

import com.google.common.collect.Sets;

@SuppressWarnings("rawtypes")
public class ConsumerConfigurationDataExt extends ConsumerConfigurationData {

	@SuppressWarnings("unchecked")
	public void setTopic(String... topics) {
		setTopicNames(Sets.newHashSet(topics));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
