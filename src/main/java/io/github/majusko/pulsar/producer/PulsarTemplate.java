package io.github.majusko.pulsar.producer;

import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.stereotype.Component;

import io.github.majusko.pulsar.collector.ProducerCollector;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;

@Component
public class PulsarTemplate {

    private final ProducerCollector producerCollector;

    public PulsarTemplate(ProducerCollector producerCollector) {
        this.producerCollector = producerCollector;
    }

	@SuppressWarnings("unchecked")
	public <T> MessageId send(String topic, T msg) throws PulsarClientException {
        return producerCollector.getProducer(topic,msg).send(msg);
    }
    
    @SuppressWarnings("unchecked")
   	public <T> MessageId send(String topic, T msg,ProducerConfigurationDataExt config) throws PulsarClientException {
           return producerCollector.getProducer(topic,msg).send(msg);
       }
}
