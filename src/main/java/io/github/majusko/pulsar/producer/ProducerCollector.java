package io.github.majusko.pulsar.producer;

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

import io.github.majusko.pulsar.annotation.PulsarProducer;
import io.github.majusko.pulsar.collector.ProducerHolder;
import io.github.majusko.pulsar.config.ProducerConfigManage;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.constant.Serialization;

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
					.collect(Collectors.toMap(ProducerHolder::getTopic, this::buildProducer)));
		}

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	private Producer<?> buildProducer(ProducerHolder holder) {
		try {
			ProducerConfigurationDataExt producerConfigurationDataExt = holder.getConfigurationDataExt();
			Schema<?> schema = getSchema(holder);
			ProducerBuilder<?> newProducer = pulsarClient.newProducer(schema);
			if (producerConfigurationDataExt != null) {
				Map<String, Object> config = producerConfigurationDataExt.toMap();
				newProducer.loadConf(config);
			} else {
				Map<String, Object> config = producerConfigManage.getConfig(holder.getTopic());
				newProducer.loadConf(config);
			}
			return newProducer.topic(holder.getTopic()).create();
		} catch (PulsarClientException e) {
			throw new RuntimeException("TODO Custom Exception!", e);
		}
	}

	private <T> Schema<?> getSchema(ProducerHolder holder) throws RuntimeException {
		if (holder.getSerialization().equals(Serialization.JSON)) {
			return Schema.JSON(holder.getClazz());
		}
		throw new RuntimeException("TODO custom runtime exception");
	}

	@SuppressWarnings("rawtypes")
	Map<String, Producer> getProducers() {
		return producers;
	}
}
