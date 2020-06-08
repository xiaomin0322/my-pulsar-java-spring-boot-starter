package io.github.majusko.pulsar;

import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Service;

import io.github.majusko.pulsar.annotation.PulsarConsumer;
import io.github.majusko.pulsar.constant.Serialization;

@Service
public class TestConsumerConfiguration {
	
	public static final Class<?> clazz = TestConsumerConfiguration.class;
	

    /*@PulsarConsumer(topic = "mock-topic", clazz = MyMsg.class, serialization = Serialization.JSON)
    public void mockTheListener(MyMsg myMsg) {
    	System.out.println("============================="+myMsg.getData());
        Assertions.assertNotNull(myMsg);
    }*/
    
    @PulsarConsumer(topic = "topic-zzm", clazz = MyMsg.class, serialization = Serialization.JSON)
    //@PulsarConsumer(clazz=MyMsg.class)
    public void topicOneTheListener(MyMsg myMsg) {
    	System.out.println("============================="+myMsg.getData());
        Assertions.assertNotNull(myMsg);
    }
}
