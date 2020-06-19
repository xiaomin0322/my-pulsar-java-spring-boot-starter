package io.github.majusko.pulsar.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优先级 1.配置文件 2.spring bean工程定义 3.用户自定义
 * 
 * @author Zengmin.Zhang
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ProducerCustomDetailConfig extends BaseCustomDetailConfig {

	private ProducerConfigurationDataExt config;

	public ProducerCustomDetailConfig() {
	}

	@Override
	public void setTopic(String topic) {
		super.setTopic(topic);
		if (getConfig() != null) {
			getConfig().setTopic(topic);
		}
	}

}
