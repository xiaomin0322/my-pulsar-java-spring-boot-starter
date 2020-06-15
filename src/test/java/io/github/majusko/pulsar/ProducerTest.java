package io.github.majusko.pulsar;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.majusko.pulsar.config.ProducerCustomDetailConfig;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import io.github.majusko.pulsar.producer.SendMessage;
import io.github.majusko.pulsar.producer.SendResult;
import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PulsarJavaSpringBootStarterApplication.class}) // 指定启动类
public class ProducerTest {

	@Autowired
	private PulsarTemplate producer;

	@Test
	public void testProducerSendMethod() throws Exception {
		SendResult send = producer.send("topic-zzm2", new MyMsg("asdasd"));
		System.out.println("=========" + send.isSucceed());
		Thread.sleep(10000);
	}
	
	@Test
	public void testProducerSendMethodSendMessage2() throws Exception {
		SendMessage<MyMsg> message = new SendMessage<>(new MyMsg("asdasd"));
		SendResult send = producer.send("persistent://dominos_dev/pe/topic-zzm2", message);
		System.out.println("=========" + send.isSucceed());
		Thread.sleep(10000);
	}
	
	/**
	 * tenant ,namespace不能自动创建。只有topic可以自动创建
	 * @throws Exception
	 */
	@Test
	public void testProducerSendMethodSendMessage3() throws Exception {
		SendMessage<MyMsg> message = new SendMessage<>(new MyMsg("asdasd"));
		SendResult send = producer.send("persistent://dominos_dev/pe/topic-zzm100", message);
		System.out.println("=========" + send.isSucceed());
		Thread.sleep(10000);
	}

	@Test
	public void testProducerSendMethodSendMessage() throws Exception {
		SendMessage<MyMsg> message = new SendMessage<>(new MyMsg("asdasd"));
		SendResult send = producer.send("topic-zzm2", message);
		System.out.println("=========" + send.isSucceed());
		Thread.sleep(10000);
	}

	@Test
	public void testProducerSendMethodConfig() throws Exception {
		SendMessage<MyMsg> message = new SendMessage<>(new MyMsg("asdasd"));
		ProducerCustomDetailConfig producerCustomDetailConfig = new ProducerCustomDetailConfig();
		producerCustomDetailConfig.setTopic("topic-zzm3");
		producerCustomDetailConfig.setClazz(MyMsg.class);
		
		SendResult send = producer.send("topic-zzm3", message, producerCustomDetailConfig);
		System.out.println("=========" + send.isSucceed());
		Thread.sleep(10000);
	}

}
