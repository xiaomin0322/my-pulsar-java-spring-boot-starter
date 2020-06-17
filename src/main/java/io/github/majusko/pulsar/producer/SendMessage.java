
package io.github.majusko.pulsar.producer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.github.majusko.pulsar.producer.SendType;

/**
 * 消息发送包装类
 * 
 * @author Zengmin.Zhang
 *
 * @param <T>
 */
public class SendMessage<T> {
	/**
	 * 排序和分片key
	 */
	private String key;

	private String tags;

	/**
	 * 延迟时间
	 */
	private Long delay;
	/**
	 * 延迟单位 默认是秒
	 */
	private TimeUnit delayTimeUnit = TimeUnit.SECONDS;

	/**
	 * 消息体对象
	 */
	private T value;

	/**
	 * 同步，异步和oneway
	 */
	private SendType sendType = SendType.SYNC;

	/**
	 * 消息属性列表
	 */
	private Map<String, String> properties;

	/**
	 * 唯一ID
	 */
	private Long sequenceId;

	public SendMessage() {
	}

	public SendMessage(T value) {
		this.value = value;
	}

	public SendMessage(String key, String tags, Long delay, T value) {
		this.key = key;
		this.tags = tags;
		this.delay = delay;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public TimeUnit getDelayTimeUnit() {
		return delayTimeUnit;
	}

	public void setDelayTimeUnit(TimeUnit delayTimeUnit) {
		this.delayTimeUnit = delayTimeUnit;
	}

	public SendType getSendType() {
		return sendType;
	}

	public void setSendType(SendType sendType) {
		this.sendType = sendType;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Long getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}

}
