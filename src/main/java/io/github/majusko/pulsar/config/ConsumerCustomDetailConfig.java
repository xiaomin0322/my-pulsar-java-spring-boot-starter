package io.github.majusko.pulsar.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * 优先级 1.配置文件 2.spring bean工程定义 3.用户自定义
 * 
 * @author Zengmin.Zhang
 *
 */
@Data
public class ConsumerCustomDetailConfig extends BaseCustomDetailConfig {

	private ConsumerConfigurationDataExt config;
	
	/**
	 *  消费者执行得method
	 */
	@JsonIgnore
	private String methodSign;
	
	
	@Override
	public void setTopic(String topic) {
		super.setTopic(topic);
		if(getConfig()!=null) {
			getConfig().setTopic(topic);
		}
	}
}
