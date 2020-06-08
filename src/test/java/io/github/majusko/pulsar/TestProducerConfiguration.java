package io.github.majusko.pulsar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.majusko.pulsar.constant.Serialization;
import io.github.majusko.pulsar.producer.ProducerFactory;
import io.github.majusko.pulsar.producer.ProducerHolder;

@Configuration
public class TestProducerConfiguration {

	@Bean
	public ProducerFactory producerFactory() {

		ProducerHolder configurationDataExt = new ProducerHolder();
		configurationDataExt.setTopicName("topic-zzm");
		configurationDataExt.setSendTimeoutMs(10000);
		configurationDataExt.setClacc(MyMsg2.class);

		return new ProducerFactory().addProducer("topic-one", MyMsg.class)
				.addProducer("topic-two", MyMsg2.class, Serialization.JSON).addProducer("topic-zzm", MyMsg2.class);
	}
}
