/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.majusko.pulsar.consumer;

import java.util.Map;

import org.apache.pulsar.client.api.Message;
import org.springframework.beans.BeanUtils;

import lombok.Data;

/**
 * 消费者消息封装实体类
 */

@Data
public class ConsumeMessage<T> {

	/**
	 * topic名称
	 */
	private String topicName;

	/**
	 * 消息路由KEY
	 */
	private String key;

	/**
	 * 消息实体对象
	 */
	private T value;

	/**
	 * 消息ID
	 */
	private String messageId;

	/**
	 * 
	 */
	private long eventTime;

	/**
	 * 
	 */
	private long publishTime;

	/**
	 * 消息唯一ID
	 */
	private long sequenceId;

	private Map<String, String> properties;

	
	/**
	 * 转换
	 * @param pulsarMsg
	 * @return
	 */
	public static <T> ConsumeMessage<T> parse(Message<T> pulsarMsg) {
		ConsumeMessage<T> message = new ConsumeMessage<T>();
		BeanUtils.copyProperties(pulsarMsg, message);
		message.setEventTime(pulsarMsg.getEventTime());
		message.setPublishTime(pulsarMsg.getPublishTime());
		message.setKey(pulsarMsg.getKey());
		message.setProperties(pulsarMsg.getProperties());
		message.setTopicName(pulsarMsg.getTopicName());
		message.setValue(pulsarMsg.getValue());
		message.setMessageId(pulsarMsg.getMessageId().toString());
		message.setSequenceId(pulsarMsg.getSequenceId());
		return message;
	}

}
