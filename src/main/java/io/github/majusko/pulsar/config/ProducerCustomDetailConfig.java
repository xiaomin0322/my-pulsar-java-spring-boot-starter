package io.github.majusko.pulsar.config;

import org.apache.pulsar.client.api.Schema;

import io.github.majusko.pulsar.constant.Serialization;
import lombok.Data;

/**
 * 优先级 1.配置文件 2.spring bean工程定义 3.用户自定义
 * 
 * @author Zengmin.Zhang
 *
 */
@Data
public class ProducerCustomDetailConfig {

	private String topic;
	private Class<?> clazz;
	private Serialization serialization = Serialization.JSON;
	private ProducerConfigurationDataExt config;

	public ProducerCustomDetailConfig() {
	}

	public ProducerConfigurationDataExt getConfig() {
		config.setTopicName(topic);
		return config;
	}

	public Schema<?> getSchema() {
		Schema<?> schema = Schema.JSON(clazz);
		return schema;
	}
}
