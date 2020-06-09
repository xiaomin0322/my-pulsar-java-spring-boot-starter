package io.github.majusko.pulsar.producer;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.TypedMessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import io.github.majusko.pulsar.collector.ProducerCollector;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;

@Component
public class PulsarTemplate {

	private final ProducerCollector producerCollector;

	public PulsarTemplate(ProducerCollector producerCollector) {
		this.producerCollector = producerCollector;
	}

	@SuppressWarnings("unchecked")
	public <T> SendResult send(String topic, SendMessage<T> msg) throws PulsarClientException {
		return send(producerCollector.getProducer(topic, msg), msg);
	}

	@SuppressWarnings("unchecked")
	public <T> SendResult send(String topic, SendMessage<T> msg, ProducerConfigurationDataExt config)
			throws PulsarClientException {
		return send(producerCollector.getProducer(topic, msg, config), msg);
	}

	private <T> SendResult check(Producer<T> producer, SendMessage<T> msg) throws PulsarClientException {
		if (producer == null || msg == null || msg.getValue() == null) {
			return SendResult.FAILURE_NULL;
		}
		return SendResult.SUCCESS;
	}

	@SuppressWarnings("unused")
	private <T> SendResult send(Producer<T> producer, SendMessage<T> msg) throws PulsarClientException {
		SendResult checkSendResult = check(producer, msg);
		if (!checkSendResult.isSucceed()) {
			return checkSendResult;
		}

		TypedMessageBuilder<T> messageBuilder = producer.newMessage();
		if (StringUtils.isNotBlank(msg.getKey())) {
			messageBuilder.key(msg.getKey());
		}
		if (!CollectionUtils.isEmpty(msg.getProperties())) {
			messageBuilder.properties(msg.getProperties());
		}

		if (msg.getDelay() != null) {
			messageBuilder.deliverAfter(msg.getDelay(), msg.getDelayTimeUnit());
		}

		if (null != msg.getSequenceId()) {
			messageBuilder.sequenceId(msg.getSequenceId());
		}

		messageBuilder.value(msg.getValue());

		if (SendType.SYNC == msg.getSendType()) {
			MessageId msgID = messageBuilder.send();
			return SendResult.getSendResult(msgID);
		} else {
			CompletableFuture<MessageId> sendAsync = messageBuilder.sendAsync();
			SendResult sendResult = new SendResult(sendAsync);
			return sendResult;
		}

	}

}
