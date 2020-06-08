package io.github.majusko.pulsar.config;

import java.io.IOException;
import java.util.Map;

import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.impl.conf.ConfigurationDataUtils;
import org.apache.pulsar.client.impl.conf.ConsumerConfigurationData;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.majusko.pulsar.constant.Serialization;

public class ConsumerConfigurationDataExt<T> extends ConsumerConfigurationData<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String clazz;

	private Class<?> clacc;

	public Class<?> getClacc() {
		return clacc;
	}

	public void setClacc(Class<?> clacc) {
		this.clacc = clacc;
	}

	public void setSchema(Schema<?> schema) {
		this.schema = schema;
	}

	private Serialization serialization = Serialization.JSON;

	@SuppressWarnings("unused")
	private Schema<?> schema;

	@SuppressWarnings("unused")
	public Schema<?> getSchema() throws RuntimeException {
		if (Serialization.JSON.equals(Serialization.JSON)) {
			try {
				if (clacc == null) {
					clacc = Class.forName(clazz);
				}
				return Schema.JSON(clacc);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("TODO custom runtime exception");
			}
		}
		throw new RuntimeException("TODO custom runtime exception");
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public Serialization getSerialization() {
		return serialization;
	}

	public void setSerialization(Serialization serialization) {
		this.serialization = serialization;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap() {
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
