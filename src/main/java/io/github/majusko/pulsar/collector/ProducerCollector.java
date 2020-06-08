package io.github.majusko.pulsar.collector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.annotation.PulsarProducer;
import io.github.majusko.pulsar.config.ProducerConfigManage;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.constant.Serialization;
import io.github.majusko.pulsar.producer.ProducerHolder;
import io.github.majusko.pulsar.producer.PulsarProducerFactory;

@Component
public class ProducerCollector implements BeanPostProcessor {

	private final PulsarClient pulsarClient;

	private ProducerConfigManage producerConfigManage;

	@SuppressWarnings("rawtypes")
	private Map<String, Producer> producers = new ConcurrentHashMap<>();

	public ProducerCollector(PulsarClient pulsarClient, ProducerConfigManage producerConfigManage) {
		this.pulsarClient = pulsarClient;
		this.producerConfigManage = producerConfigManage;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		final Class<?> beanClass = bean.getClass();

		if (beanClass.isAnnotationPresent(PulsarProducer.class) && bean instanceof PulsarProducerFactory) {
			producers.putAll(((PulsarProducerFactory) bean).getTopics().values().stream()
					.collect(Collectors.toMap(ProducerHolder::getTopicName, this::buildProducer)));
		}

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	private Producer<?> buildProducer(ProducerHolder holder) {
		try {
			Schema<?> schema = holder.getSchema();
			ProducerBuilder<?> newProducer = pulsarClient.newProducer(schema);
			Map<String, Object> config = producerConfigManage.getConfig(holder.getTopicName());
			if (!CollectionUtils.isEmpty(config)) {
				newProducer.loadConf(config);
			}
			return newProducer.topic(holder.getTopicName()).create();
		} catch (PulsarClientException e) {
			throw new RuntimeException("TODO Custom Exception!", e);
		}
	}

	@SuppressWarnings("rawtypes")
	Map<String, Producer> getProducers() {
		return producers;
	}
}
