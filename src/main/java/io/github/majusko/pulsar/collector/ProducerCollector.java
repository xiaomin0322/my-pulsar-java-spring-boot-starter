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
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.github.majusko.pulsar.annotation.PulsarProducer;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.producer.ProducerHolder;
import io.github.majusko.pulsar.producer.PulsarProducerFactory;
import io.github.majusko.pulsar.producer.SendMessage;
import io.github.majusko.pulsar.util.ConfigurationDataUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProducerCollector implements BeanPostProcessor, CommandLineRunner {

	private final PulsarClient pulsarClient;

	@SuppressWarnings("rawtypes")
	private Map<String, Producer> producers = new ConcurrentHashMap<>();

	public ProducerCollector(PulsarClient pulsarClient) {
		this.pulsarClient = pulsarClient;
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
			Schema<?> schema = Schema.JSON(holder.getClazz());
			ProducerBuilder<?> newProducer = pulsarClient.newProducer(schema);
			ProducerConfigurationDataExt config = holder.getConfig();
			if (config != null) {
				newProducer.loadConf(ConfigurationDataUtils.toMap(config, ProducerConfigurationDataExt.class));
			}
			return newProducer.topic(holder.getTopic()).create();
		} catch (PulsarClientException e) {
			throw new RuntimeException("TODO Custom Exception!", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public <T> Producer getProducer(String topic, SendMessage<T> msg) {
		return getProducer(topic, msg, null);
	}

	@SuppressWarnings("rawtypes")
	public <T> Producer getProducer(String topic, SendMessage<T> msg, ProducerConfigurationDataExt config) {
		Producer producer = producers.get(topic);
		;
		if (producer != null) {
			return producer;
		}
		ProducerHolder producerHolder = new ProducerHolder();
		producerHolder.setTopic(topic);
		producerHolder.setClazz(msg.getValue().getClass());
		if (config != null) {
			producerHolder.setConfig(config);
		}
		producer = buildProducer(producerHolder);
		producers.put(topic, producer);
		return producer;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Producer> getProducers() {
		return producers;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("producers topic keys {} ",producers.keySet());
		
	}
}
