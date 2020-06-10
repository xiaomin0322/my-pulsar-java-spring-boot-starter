package io.github.majusko.pulsar.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import io.github.majusko.pulsar.exception.PulsarRuntimeException;
import io.netty.util.concurrent.FastThreadLocal;

/**
 * Utils for loading configuration data.
 */
public final class ConfigurationDataUtils {

	public static ObjectMapper create() {
		ObjectMapper mapper = new ObjectMapper();
		// forward compatibility for the properties may go away in the future
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper;
	}

	private static final FastThreadLocal<ObjectMapper> mapper = new FastThreadLocal<ObjectMapper>() {
		@Override
		protected ObjectMapper initialValue() throws Exception {
			return create();
		}
	};

	public static ObjectMapper getThreadLocal() {
		return mapper.get();
	}

	private ConfigurationDataUtils() {
	}

	public static <T> T toData(Map<String, Object> config, Class<T> dataCls) {
		ObjectMapper mapper = getThreadLocal();
		try {
			Map<String, Object> newConfig = Maps.newHashMap();
			newConfig.putAll(config);
			String configJson = mapper.writeValueAsString(newConfig);
			return mapper.readValue(configJson, dataCls);
		} catch (IOException e) {
			throw new PulsarRuntimeException("Failed to load config into existing configuration data", e);
		}

	}

	public static <T> Map<String, Object> toMap(T data, Class<T> dataCls) {
		ObjectMapper mapper = getThreadLocal();
		try {
			String existingConfigJson = mapper.writeValueAsString(data);
			@SuppressWarnings("unchecked")
			Map<String, Object> existingConfig = mapper.readValue(existingConfigJson, Map.class);
			return existingConfig;
		} catch (IOException e) {
			throw new PulsarRuntimeException("Failed to load config into existing configuration data", e);
		}

	}

}
