package io.github.majusko.pulsar;

import org.apache.pulsar.client.impl.conf.ClientConfigurationData;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
	private String serviceUrl = "pulsar://localhost:6650";
	private Integer ioThreads = 10;
	private Integer listenerThreads = 10;
	private boolean enableTcpNoDelay = false;
	private Integer keepAliveIntervalSec = 20;
	private Integer connectionTimeoutSec = 10;
	private Integer operationTimeoutSec = 15;
	private Integer startingBackoffIntervalMs = 100;
	private Integer maxBackoffIntervalSec = 10;
	private String  token;

}