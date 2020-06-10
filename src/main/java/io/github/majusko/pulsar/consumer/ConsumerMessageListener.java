package io.github.majusko.pulsar.consumer;

import java.lang.reflect.Method;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

import io.github.majusko.pulsar.exception.PulsarRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerMessageListener<T> implements MessageListener<T> {
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
			log.info("receivedException", e);
			consumer.negativeAcknowledge(msg);
			throw new PulsarRuntimeException("TODO Custom Exception!", e);
		}

	}

}