package io.github.majusko.pulsar.collector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.annotation.PulsarProducer;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.config.ProducerCustomConfig;
import io.github.majusko.pulsar.config.ProducerCustomDetailConfig;
import io.github.majusko.pulsar.producer.ProducerHolder;
import io.github.majusko.pulsar.producer.PulsarProducerFactory;
import io.github.majusko.pulsar.producer.SendMessage;
import io.github.majusko.pulsar.util.ConfigurationDataUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProducerCollector implements BeanPostProcessor, CommandLineRunner {

	private final PulsarClient pulsarClient;

	private final ProducerCustomConfig producerCustomConfig;

	@SuppressWarnings("rawtypes")
	private Map<String, Producer> producers = new ConcurrentHashMap<>();

	public ProducerCollector(PulsarClient pulsarClient, ProducerCustomConfig producerCustomConfig) {
		this.pulsarClient = pulsarClient;
		this.producerCustomConfig = producerCustomConfig;
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

	@SuppressWarnings("rawtypes")
	public Producer getProducer(String topic) {
		return getProducerMap().get(topic);
	}

	@SuppressWarnings("rawtypes")
	public void addProducer(String topic, Producer producer) {
		if (StringUtils.isBlank(topic) || producer == null) {
			return;
		}
		getProducerMap().put(topic, producer);
	}

	public void init() {
		// 覆盖 spring bean 配置得对象
		Map<String, ProducerCustomDetailConfig> producersMap = producerCustomConfig.getProducer();
		if (!CollectionUtils.isEmpty(producersMap)) {
			producers.putAll(producersMap.values().stream()
					.collect(Collectors.toMap(ProducerCustomDetailConfig::getTopic, this::buildProducer)));
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	private Producer<?> buildProducer(ProducerCustomDetailConfig holder) {
		try {
			Schema<?> schema = holder.schema();
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
	public <T> Producer getProducer(String topic, SendMessage<T> msg, ProducerCustomDetailConfig config) {
		Producer producer = getProducer(topic);
		if (producer != null) {
			return producer;
		}
		if (config == null) {
			config = ProducerHolder.getDef(topic);
		}
		producer = buildProducer(config);
		addProducer(topic, producer);
		return producer;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Producer> getProducerMap() {
		if (!CollectionUtils.isEmpty(producers)) {
			return producers;
		}
		synchronized (producers) {
			if (!CollectionUtils.isEmpty(producers)) {
				return producers;
			}
			init();
		}
		return producers;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("producers topic keys {} ", producers.keySet());

	}
}
