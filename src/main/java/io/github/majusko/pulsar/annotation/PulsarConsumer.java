package io.github.majusko.pulsar.annotation;

import io.github.majusko.pulsar.constant.Serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.pulsar.client.api.SubscriptionType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PulsarConsumer {

	/**
	 * topic 名称 {persistent|non-persistent}://tenant/namespace/topic
	 */
	String topic();

	/**
	 * 发送消息类得Class对象
	 */
	Class<?> clazz();

	/**
	 * 序列化方式，默认JSON
	 */
	Serialization serialization() default Serialization.JSON;

	/**
	 * 订阅方式默认共享模式 Shared:共享模式不保证有序，如果对有序有要求使用failover
	 * 更多参考：http://pulsar.apache.org/docs/en/concepts-messaging/#subscriptions
	 */
	SubscriptionType subscriptionType() default SubscriptionType.Shared;

	/**
	 * 自定义配置Class类，在该class中实现一个返回值为 ConsumerConfigurationDataExt对象
	 * 更多参考：http://pulsar.apache.org/docs/en/concepts-messaging/#consumers
	 * 
	 */
	Class<?>[] configuration() default {};
}
