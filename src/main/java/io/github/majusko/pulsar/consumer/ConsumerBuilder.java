package io.github.majusko.pulsar.consumer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.collector.ConsumerCollector;
import io.github.majusko.pulsar.collector.ConsumerHolder;
import io.github.majusko.pulsar.config.ConsumerConfigManage;

@Component
@DependsOn({ "pulsarClient", "consumerCollector" })
public class ConsumerBuilder {

	private final ConsumerCollector consumerCollector;
	private final PulsarClient pulsarClient;
	private final ConsumerConfigManage consumerConfigManage;

	@SuppressWarnings("rawtypes")
	private List<Consumer> consumers;

	public ConsumerBuilder(ConsumerCollector consumerCollector, PulsarClient pulsarClient,
			ConsumerConfigManage consumerConfigManage) {
		this.consumerCollector = consumerCollector;
		this.pulsarClient = pulsarClient;
		this.consumerConfigManage = consumerConfigManage;
	}

	@PostConstruct
	private void init() {
		consumers = consumerCollector.getConsumers().entrySet().stream()
				.map(holder -> subscribe(holder.getKey(), holder.getValue())).collect(Collectors.toList());
	}

	private Consumer<?> subscribe(String name, ConsumerHolder holder) {
		try {
			PulsarConsumer annotation = holder.getAnnotation();
			Map<String, Object> config = consumerConfigManage.getConfig(annotation.topic());
			org.apache.pulsar.client.api.ConsumerBuilder<?> consumerBuilder = pulsarClient
					.newConsumer(Schema.JSON(holder.getAnnotation().clazz()))
					.subscriptionType(annotation.subscriptionType()).consumerName("consumer-" + name)
					.subscriptionName("subscription-" + name).topic(holder.getAnnotation().topic())
					.messageListener((consumer, msg) -> {
						try {
							final Method method = holder.getHandler();

							method.setAccessible(true);
							method.invoke(holder.getBean(), msg.getValue());

							consumer.acknowledge(msg);
						} catch (Exception e) {
							consumer.negativeAcknowledge(msg);
							throw new RuntimeException("TODO Custom Exception!", e);
						}
					});

			if (!CollectionUtils.isEmpty(config)) {
				consumerBuilder.loadConf(config);
			}
			return consumerBuilder.subscribe();
		} catch (PulsarClientException e) {
			throw new RuntimeException("TODO Custom Exception!", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Consumer> getConsumers() {
		return consumers;
	}
}
