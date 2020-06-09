package io.github.majusko.pulsar.config;

import org.apache.pulsar.client.api.Schema;

import io.github.majusko.pulsar.constant.Serialization;
import lombok.Data;

@Data
public class BaseCustomDetailConfig {

	private String topic;
	private Class<?> clazz;
	private Serialization serialization = Serialization.JSON;

	public BaseCustomDetailConfig() {
	}

	
	public Schema<?> schema() {
		Schema<?> schema = Schema.JSON(getClazz());
		return schema;
	}
}
