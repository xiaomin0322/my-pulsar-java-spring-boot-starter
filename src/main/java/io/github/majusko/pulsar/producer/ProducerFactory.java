package io.github.majusko.pulsar.producer;

import java.util.HashMap;
import java.util.Map;

import io.github.majusko.pulsar.annotation.PulsarProducer;
import io.github.majusko.pulsar.collector.ProducerHolder;
import io.github.majusko.pulsar.constant.Serialization;

@PulsarProducer
public class ProducerFactory implements PulsarProducerFactory {

	/**
	 * org.apache.pulsar.client.impl.ProducerBuilderImpl<T>
	 */
	private final Map<String, ProducerHolder> topics = new HashMap<>();

	public ProducerFactory addProducer(String topic, Class<?> clazz) {
		ProducerHolder producerHolder = new ProducerHolder(topic, clazz, Serialization.JSON);
		topics.put(topic, producerHolder);
		return this;
	}

	public ProducerFactory addProducer(String topic, Class<?> clazz, Serialization serialization) {
		ProducerHolder configurationData = new ProducerHolder(topic, clazz, serialization);
		topics.put(topic, configurationData);
		return this;
	}
	
	public ProducerFactory addProducer(String topic, Class<?> clazz,Serialization serialization,ProducerConfigurationDataExt configurationData) {
		ProducerHolder producerHolder = new ProducerHolder(topic, clazz, serialization);
		if (configurationData != null) {
			producerHolder.setConfigurationDataExt(configurationData);
		}
		topics.put(topic, producerHolder);
		return this;
	}

	public ProducerFactory addProducer(String topic, Class<?> clazz, ProducerConfigurationDataExt configurationData) {
		ProducerHolder producerHolder = new ProducerHolder(topic, clazz, Serialization.JSON);
		;
		if (configurationData != null) {
			producerHolder.setConfigurationDataExt(configurationData);
		}
		topics.put(topic, producerHolder);
		return this;
	}

	public Map<String, ProducerHolder> getTopics() {
		return topics;
	}

}
