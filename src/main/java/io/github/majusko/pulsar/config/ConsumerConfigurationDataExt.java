package io.github.majusko.pulsar.config;

import java.io.IOException;
import java.util.Map;

import org.apache.pulsar.client.impl.conf.ConfigurationDataUtils;
import org.apache.pulsar.client.impl.conf.ConsumerConfigurationData;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumerConfigurationDataExt<T> extends ConsumerConfigurationData<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public  Map<String, Object> toMap() {
		ObjectMapper mapper = ConfigurationDataUtils.getThreadLocal();
		try {
			String existingConfigJson = mapper.writeValueAsString(this);
			Map<String, Object> existingConfig = mapper.readValue(existingConfigJson, Map.class);
			return existingConfig;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config into existing configuration data", e);
		}
	}
}
