package io.github.majusko.pulsar;

import java.util.List;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.majusko.pulsar.collector.ConsumerCollector;
import io.github.majusko.pulsar.config.ConsumerCustomConfig;
import io.github.majusko.pulsar.consumer.ConsumerBuilder;
import io.github.majusko.pulsar.consumer.ConsumerHolder;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import io.github.majusko.pulsar.producer.SendMessage;
import io.github.majusko.pulsar.producer.SendResult;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({ TestProducerConfiguration.class, TestConsumerConfiguration.class })
class PulsarJavaSpringBootStarterApplicationTests {

	@Autowired
	private ConsumerBuilder consumerBuilder;

	@Autowired
	private ConsumerCollector consumerCollector;


	@Autowired
	private PulsarTemplate producer;

	@Test
	void testConsumer() throws PulsarClientException {
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testProducerSendMethod2() throws PulsarClientException {
		SendMessage<MyMsg> message = new SendMessage<>();
		SendResult send = producer.send("topic-zzm", message);
		System.out.println("=========" + send.getMsgId());
	}

	@Test
	void testProducerSendMethod22() throws PulsarClientException {
		SendMessage<MyMsg> message = new SendMessage<>(new MyMsg("asdasd"));
		SendResult send = producer.send("topic-zzm2", message);
		System.out.println("=========" + send.isSucceed());
	}

	@Test
	void testProducerSendMethod() throws PulsarClientException {
		// MessageId send = producer.send("topic-one", new MyMsg("bb"));
		// System.out.println("=========" + send.toString());
	}

	@Test
	void testConsumerRegistration1() throws Exception {
		final List<Consumer> consumers = consumerBuilder.getConsumers();

		Assertions.assertEquals(1, consumers.size());

		final Consumer consumer = consumers.stream().findFirst().orElseThrow(Exception::new);

		Assertions.assertNotNull(consumer);
		Assertions.assertEquals("mock-topic", consumer.getTopic());
	}

	@Test
	void testConsumerRegistration2() {
		final Class<TestConsumerConfiguration> clazz = TestConsumerConfiguration.class;
		final String descriptor = clazz.getName() + "#" + clazz.getMethods()[0].getName();
		final ConsumerHolder consumerHolder = consumerCollector.getConsumer(descriptor).orElse(null);

		Assertions.assertNotNull(consumerHolder);
		Assertions.assertEquals("mock-topic", consumerHolder.getAnnotation().topic());
		Assertions.assertEquals(TestConsumerConfiguration.class, consumerHolder.getBean().getClass());
		Assertions.assertEquals("mockTheListener", consumerHolder.getHandler().getName());
	}

	/*@Test
	void testProducerRegistration() {

		Map<String, ProducerHolder> topics = producerFactory.getTopics();

		Assertions.assertEquals(2, topics.size());

		final Set<String> topicNames = new HashSet<>(topics.keySet());

		Assertions.assertTrue(topicNames.contains("topic-one"));
		Assertions.assertTrue(topicNames.contains("topic-two"));
	}*/
	
	@Autowired
	ConsumerCustomConfig consumerCustomConfig;
	
	@Test
	void testConfig() {
		System.out.println(consumerCustomConfig.getConsumer().get("topic-zzm2").getConfig().getTopic());
		System.out.println(consumerCustomConfig.getConsumer().get("topic-zzm2").getMethodSign());
	}
}
