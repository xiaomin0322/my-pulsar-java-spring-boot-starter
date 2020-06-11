package io.github.majusko.pulsar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.consumer.ConsumeMessage;

@Service
public class TestConsumerConfiguration {

	@Autowired
	TestService testService;

	@PulsarConsumer(topic = "topic-zzm", clazz = MyMsg.class)
	public void topicOneTheListener(MyMsg myMsg) {
		System.out.println("=============================" + myMsg.getData());
		//Assertions.assertNotNull(myMsg);
	}

	// @PulsarConsumer(topic = "topic-zzm", clazz = MyMsg.class, configuration = {ConsumerConfig.class })
	public void topicOneTheListener2(ConsumeMessage<MyMsg> myMsg) {
		testService.print("===============1111111111111111==============" + myMsg.getValue().getData());
		//Assertions.assertNotNull(myMsg);
	}

}
