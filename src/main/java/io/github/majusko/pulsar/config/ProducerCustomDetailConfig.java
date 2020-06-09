package io.github.majusko.pulsar.config;

import lombok.Data;

/**
 * 优先级 1.配置文件 2.spring bean工程定义 3.用户自定义
 * 
 * @author Zengmin.Zhang
 *
 */
@Data
public class ProducerCustomDetailConfig extends BaseCustomDetailConfig {

	private ProducerConfigurationDataExt config;

	public ProducerCustomDetailConfig() {
	}

	public ProducerConfigurationDataExt getConfig() {
	    if(config!=null) {
	    	config.setTopicName(getTopic());
	    }	
		return config;
	}

}
