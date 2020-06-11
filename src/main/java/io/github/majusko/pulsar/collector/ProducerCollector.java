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
import io.github.majusko.pulsar.exception.PulsarRuntimeException;
import io.github.majusko.pulsar.producer.ProducerHolder;
import io.github.majusko.pulsar.producer.PulsarProducerFactory;
import io.github.majusko.pulsar.producer.SendMessage;
import io.github.majusko.pulsar.util.ConfigurationDataUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SuppressWarnings("rawtypes")
public class ProducerCollector implements BeanPostProcessor, CommandLineRunner {

	private final PulsarClient pulsarClient;

	private final ProducerCustomConfig producerCustomConfig;

	private Map<String, Producer> producers = new ConcurrentHashMap<>();

	public ProducerCollector(PulsarClient pulsarClient, ProducerCustomConfig producerCustomConfig) {
		this.pulsarClient = pulsarClient;
		this.producerCustomConfig = producerCustomConfig;
		initProducers();
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		final Class<?> beanClass = bean.getClass();
		// 注入用户spring 自定义配置
		if (beanClass.isAnnotationPresent(PulsarProducer.class) && bean instanceof PulsarProducerFactory) {
			producers.putAll(((PulsarProducerFactory) bean).getTopics().values().stream()
					.filter($ -> !producers.containsKey($.getTopic()))
					.collect(Collectors.toMap(ProducerHolder::getTopic, this::buildProducer)));
		}
		return bean;
	}

	public Producer getProducer(String topic) {
		return getProducerMap().get(topic);
	}

	public void addProducer(String topic, Producer producer) {
		if (StringUtils.isBlank(topic) || producer == null) {
			return;
		}
		getProducerMap().put(topic, producer);
	}

	/**
	 * 初始化yml生产者配置配置
	 */
	public void initProducers() {
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
		} catch (Exception e) {
			throw new PulsarRuntimeException("TODO Custom Exception!", e);
		}
	}

	public <T> Producer getProducer(String topic, SendMessage<T> msg) {
		return getProducer(topic, msg, null);
	}

	public <T> Producer getProducer(String topic, SendMessage<T> msg, ProducerCustomDetailConfig config) {
		// 获取yml,用户自定义配置
		Producer producer = getProducer(topic);
		if (producer != null) {
			return producer;
		}
		// 获取默认配置
		if (config == null) {
			config = ProducerHolder.getDefConfig(topic,msg.getValue());
		}
		producer = buildProducer(config);
		addProducer(topic, producer);
		return producer;
	}

	public Map<String, Producer> getProducerMap() {
		return producers;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("producers topic keys {} ", producers.keySet());

	}
}
