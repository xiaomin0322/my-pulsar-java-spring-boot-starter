package io.github.majusko.pulsar.consumer;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import io.github.majusko.pulsar.collector.ConsumerCollector;
import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;
import io.github.majusko.pulsar.config.ConsumerCustomConfig;
import io.github.majusko.pulsar.exception.PulsarRuntimeException;
import io.github.majusko.pulsar.util.ConfigurationDataUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@DependsOn({ "pulsarClient", "consumerCollector" })
@Slf4j
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConsumerBuilder {

	private final ConsumerCollector consumerCollector;
	private final PulsarClient pulsarClient;

	private List<Consumer> consumers;
	
	@Autowired
	private ConsumerCustomConfig consumerCustomConfig;

	public ConsumerBuilder(ConsumerCollector consumerCollector, PulsarClient pulsarClient) {
		this.consumerCollector = consumerCollector;
		this.pulsarClient = pulsarClient;
	}

	@PostConstruct
	private void init() {
		consumers = consumerCollector.getConsumers().entrySet().stream()
				.map(holder -> subscribe(holder.getKey(), holder.getValue())).collect(Collectors.toList());
	}

	private Consumer<?> subscribe(String name, ConsumerHolder holder) {
		try {
			Schema<?> schema = holder.schema();
			org.apache.pulsar.client.api.ConsumerBuilder<?> consumerBuilder = pulsarClient.newConsumer(schema);
			ConsumerConfigurationDataExt config = holder.getConfig(consumerCustomConfig.getConsumer());
			if (config != null) {
				consumerBuilder = consumerBuilder
						.loadConf(ConfigurationDataUtils.toMap(config, ConsumerConfigurationDataExt.class));
			} else {
				log.error("Consumer  topic : {} not fount ConsumerConfigurationDataExt ", name);
				throw new PulsarRuntimeException(
						"Consumer  topic :" + name + " not fount ConsumerConfigurationDataExt");
			}
			consumerBuilder = consumerBuilder.messageListener(new ConsumerMessageListener(holder));
			log.info("consumer : {} subscribed ", name);
			return consumerBuilder.subscribe();
		} catch (PulsarClientException e) {
			throw new PulsarRuntimeException("TODO Custom Exception!", e);
		}
	}

	public List<Consumer> getConsumers() {
		return consumers;
	}
}
