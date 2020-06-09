package io.github.majusko.pulsar.consumer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.collector.ConsumerCollector;
import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;
import io.github.majusko.pulsar.util.ConfigurationDataUtils;

@Component
@DependsOn({ "pulsarClient", "consumerCollector" })
public class ConsumerBuilder {

	private final ConsumerCollector consumerCollector;
	private final PulsarClient pulsarClient;

	@SuppressWarnings("rawtypes")
	private List<Consumer> consumers;

	public ConsumerBuilder(ConsumerCollector consumerCollector, PulsarClient pulsarClient) {
		this.consumerCollector = consumerCollector;
		this.pulsarClient = pulsarClient;
	}

	@PostConstruct
	private void init() {
		consumers = consumerCollector.getConsumers().entrySet().stream()
				.map(holder -> subscribe(holder.getKey(), holder.getValue())).collect(Collectors.toList());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Consumer<?> subscribe(String name, ConsumerHolder holder) {
		try {
			Schema<?> schema = Schema.JSON(holder.getAnnotation().clazz());
			org.apache.pulsar.client.api.ConsumerBuilder<?> consumerBuilder = pulsarClient.newConsumer(schema);
			ConsumerConfigurationDataExt config = holder.getConfig();
			if (config != null) {
				consumerBuilder = consumerBuilder
						.loadConf(ConfigurationDataUtils.toMap(config, ConsumerConfigurationDataExt.class));
			}
			consumerBuilder.messageListener(new ConsumerMessageListener(holder));
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

class ConsumerMessageListener<T> implements MessageListener<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ConsumerHolder holder;

	public ConsumerMessageListener(ConsumerHolder holder) {
		this.holder = holder;
	}

	@Override
	public void received(Consumer<T> consumer, Message<T> msg) {
		try {
			final Method method = holder.getHandler();
			Class<?> returnType = method.getParameterTypes()[0];
			Object value = msg.getValue();
			if (!returnType.equals(value.getClass())) {
				value = ConsumeMessage.parse(msg);
			}
			method.setAccessible(true);
			method.invoke(holder.getBean(), value);
			consumer.acknowledge(msg);
		} catch (Exception e) {
			consumer.negativeAcknowledge(msg);
			throw new RuntimeException("TODO Custom Exception!", e);
		}

	}

}
