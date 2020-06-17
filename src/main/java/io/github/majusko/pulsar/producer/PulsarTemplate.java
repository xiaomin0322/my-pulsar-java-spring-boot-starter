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
import io.github.majusko.pulsar.config.ProducerCustomDetailConfig;
import io.github.majusko.pulsar.exception.PulsarRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Component
@SuppressWarnings("unchecked")
@Slf4j
public class PulsarTemplate {

	private final ProducerCollector producerCollector;

	public PulsarTemplate(ProducerCollector producerCollector) {
		this.producerCollector = producerCollector;
	}

	/**
	 * 
	 * 发送消息
	 * 
	 * @param topic topic名称
	 * @param msg   消息对象
	 * @return SendResult
	 * @throws PulsarRuntimeException
	 */
	public <T> SendResult send(String topic, T msg) throws PulsarRuntimeException {
		return send(topic, new SendMessage<T>(msg));
	}

	/**
	 * 发现消息
	 * 
	 * @param topic  topic名称
	 * @param msg    消息对象
	 * @param config 自定义配置对象
	 * @return SendResult
	 * @throws PulsarRuntimeException
	 */
	public <T> SendResult send(String topic, T msg, ProducerCustomDetailConfig config) throws PulsarRuntimeException {
		return send(topic, new SendMessage<T>(msg), config);
	}

	/**
	 * 发现消息
	 * 
	 * @param topic topic名称
	 * @param msg   消息对象
	 * @return SendResult
	 * @throws PulsarRuntimeException
	 */
	public <T> SendResult send(String topic, SendMessage<T> msg) throws PulsarRuntimeException {
		return send(producerCollector.getProducer(topic, msg), msg);
	}

	/**
	 * 发送消息
	 * 
	 * @param topic  topic名称
	 * @param msg    消息对象
	 * @param config 自定义配置对象
	 * @return SendResult
	 * @throws PulsarRuntimeException
	 */
	public <T> SendResult send(String topic, SendMessage<T> msg, ProducerCustomDetailConfig config)
			throws PulsarRuntimeException {
		return send(producerCollector.getProducer(topic, msg, config), msg);
	}

	/**
	 * 检查消息
	 * 
	 * @param producer 发送者对象
	 * @param msg      消息对象
	 * @return SendResult
	 * @throws PulsarRuntimeException
	 */
	private <T> SendResult check(Producer<T> producer, SendMessage<T> msg) throws PulsarRuntimeException {
		if (producer == null || msg == null || msg.getValue() == null) {
			return SendResult.FAILURE_NULL;
		}
		return SendResult.SUCCESS;
	}

	/**
	 * 发送消息
	 * 
	 * @param producer 发送者
	 * @param msg      消息试提
	 * @return
	 * @throws PulsarRuntimeException
	 */
	private <T> SendResult send(Producer<T> producer, SendMessage<T> msg) throws PulsarRuntimeException {
		SendResult checkSendResult = check(producer, msg);
		if (!checkSendResult.isSucceed()) {
			return checkSendResult;
		}
		TypedMessageBuilder<T> messageBuilder = messageBuilder(producer, msg);
		if (SendType.SYNC == msg.getSendType()) {
			MessageId msgID = null;
			try {
				msgID = messageBuilder.send();
			} catch (PulsarClientException e) {
				log.error("sendException", e);
				throw new PulsarRuntimeException("sendException", e);
			}
			return SendResult.getSendResult(msgID);
		} else {
			CompletableFuture<MessageId> sendAsync = messageBuilder.sendAsync();
			SendResult sendResult = new SendResult(sendAsync);
			return sendResult;
		}

	}

	/**
	 * 构建消息
	 * 
	 * @param producer 发送者
	 * @param msg      消息对象
	 * @return
	 */
	private <T> TypedMessageBuilder<T> messageBuilder(Producer<T> producer, SendMessage<T> msg) {
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
		return messageBuilder;
	}

}
