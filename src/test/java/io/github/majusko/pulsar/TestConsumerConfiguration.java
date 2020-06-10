package io.github.majusko.pulsar;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.constant.Serialization;
import io.github.majusko.pulsar.consumer.ConsumeMessage;

@Service
public class TestConsumerConfiguration {

	@Autowired
	TestService testService;

	public static final Class<?> clazz = TestConsumerConfiguration.class;

	/*
	 * @PulsarConsumer(topic = "mock-topic", clazz = MyMsg.class, serialization =
	 * Serialization.JSON) public void mockTheListener(MyMsg myMsg) {
	 * System.out.println("============================="+myMsg.getData());
	 * Assertions.assertNotNull(myMsg); }
	 */

	@PulsarConsumer(topic = "topic-zzm", clazz = MyMsg.class, serialization = Serialization.JSON)
	public void topicOneTheListener(MyMsg myMsg) {
		System.out.println("=============================" + myMsg.getData());
		Assertions.assertNotNull(myMsg);
	}

	// @PulsarConsumer(topic = "topic-zzm2", clazz = MyMsg.class)
	@PulsarConsumer(topic = "topic-zzm2", clazz = MyMsg.class, configuration = { ConsumerConfig.class })
	public void topicOneTheListener2(ConsumeMessage<MyMsg> myMsg) {
		// System.out.println("===============1111111111111111==============" +
		// myMsg.getValue().getData());
		testService.print("===============1111111111111111==============" + myMsg.getValue().getData());
		Assertions.assertNotNull(myMsg);
	}
}
