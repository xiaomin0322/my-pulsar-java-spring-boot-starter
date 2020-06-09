package io.github.majusko.pulsar;

import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;

public class ConsumerConfig {
		
		public ConsumerConfigurationDataExt get() {
			ConsumerConfigurationDataExt configurationDataExt = new ConsumerConfigurationDataExt();
			configurationDataExt.setAckTimeoutMillis(33333);
			return configurationDataExt;
		}
}
