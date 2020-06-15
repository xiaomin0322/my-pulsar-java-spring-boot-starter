package io.github.majusko.pulsar;

import org.apache.pulsar.client.impl.conf.ClientConfigurationData;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "pulsar.client")
@Getter
@Setter
public class PulsarProperties extends ClientConfigurationData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String token;

}