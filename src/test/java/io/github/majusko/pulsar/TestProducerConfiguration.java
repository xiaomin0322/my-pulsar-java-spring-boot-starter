package io.github.majusko.pulsar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.majusko.pulsar.constant.Serialization;
import io.github.majusko.pulsar.producer.ProducerConfigurationDataExt;
import io.github.majusko.pulsar.producer.ProducerFactory;

@Configuration
public class TestProducerConfiguration {

	@Bean
	public ProducerFactory producerFactory() {

		ProducerConfigurationDataExt configurationDataExt = new ProducerConfigurationDataExt();
		configurationDataExt.setSendTimeoutMs(10000);

		return new ProducerFactory().addProducer("topic-one", MyMsg.class)
				.addProducer("topic-two", MyMsg2.class, Serialization.JSON)
				.addProducer("topic-zzm", MyMsg2.class, Serialization.JSON, configurationDataExt);
	}
}
