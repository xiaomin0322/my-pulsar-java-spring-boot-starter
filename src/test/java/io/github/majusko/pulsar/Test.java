package io.github.majusko.pulsar;

import java.util.Map;

import org.apache.pulsar.client.api.SubscriptionType;

import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;

public class Test {
	
	public static void main(String[] args) {
		ProducerConfigurationDataExt configurationData = new ProducerConfigurationDataExt();
		
		System.out.println(configurationData.toMap());
		
		ConsumerConfigurationDataExt configurationDataExt = new ConsumerConfigurationDataExt<>();
		configurationDataExt.setSubscriptionType(SubscriptionType.Failover);
		Map map = configurationDataExt.toMap();
		System.out.println(map);
		
		
	}

}
