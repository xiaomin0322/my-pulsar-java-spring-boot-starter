package io.github.majusko.pulsar.config;

import io.github.majusko.pulsar.constant.Serialization;
import lombok.Data;

@Data
public class ConsumerCustomDetailConfig{
	
	private String topic;
	private Class<?> clazz;
	private Serialization serialization = Serialization.JSON;
	private ConsumerConfigurationDataExt config;
}
