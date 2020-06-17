package io.github.majusko.pulsar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.majusko.pulsar.producer.ProducerFactory;
import io.github.majusko.pulsar.producer.ProducerHolder;

@Configuration
public class TestProducerConfiguration {

	//@Bean
	public ProducerFactory producerFactory() {
		ProducerHolder configurationDataExt = new ProducerHolder();
		configurationDataExt.setTopic("topic-zzm");
		configurationDataExt.setClazz(MyMsg2.class);
		return new ProducerFactory().addProducer(configurationDataExt);
	}
}
