package io.github.majusko.pulsar.producer;

import java.util.HashMap;
import java.util.Map;

import io.github.majusko.pulsar.annotation.PulsarProducer;
import io.github.majusko.pulsar.constant.Serialization;

@PulsarProducer
public class ProducerFactory implements PulsarProducerFactory {

	/**
	 * org.apache.pulsar.client.impl.ProducerBuilderImpl<T>
	 */
	private final Map<String, ProducerHolder> topics = new HashMap<>();

	public ProducerFactory addProducer(String topic, Class<?> clacc) {
		ProducerHolder producerHolder = new ProducerHolder();
		producerHolder.setTopic(topic);
		producerHolder.setClazz(clacc);
		topics.put(topic, producerHolder);
		return this;
	}

	public ProducerFactory addProducer(String topic, Class<?> clacc, Serialization serialization) {
		ProducerHolder producerHolder = new ProducerHolder();
		producerHolder.setTopic(topic);
		producerHolder.setClazz(clacc);
		producerHolder.setSerialization(serialization);
		topics.put(topic, producerHolder);
		return this;
	}

	public ProducerFactory addProducer(ProducerHolder holder) {
		if (holder == null) {
			return this;
		}
		topics.put(holder.getTopic(), holder);
		return this;
	}

	@Override
	public Map<String, ProducerHolder> getTopics() {
		return topics;
	}

}
