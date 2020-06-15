package io.github.majusko.pulsar;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "pulsar.client")
@Data
public class PulsarProperties {
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