package io.github.majusko.pulsar;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableConfigurationProperties(PulsarProperties.class)
public class PulsarAutoConfiguration {

	private final PulsarProperties pulsarProperties;

	public PulsarAutoConfiguration(PulsarProperties pulsarProperties) {
		this.pulsarProperties = pulsarProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public PulsarClient pulsarClient() throws PulsarClientException {
		ClientBuilder maxBackoffInterval = PulsarClient.builder().serviceUrl(pulsarProperties.getServiceUrl())
				.ioThreads(pulsarProperties.getIoThreads()).listenerThreads(pulsarProperties.getListenerThreads())
				.enableTcpNoDelay(pulsarProperties.isEnableTcpNoDelay())
				.keepAliveInterval(pulsarProperties.getKeepAliveIntervalSec(), TimeUnit.SECONDS)
				.connectionTimeout(pulsarProperties.getConnectionTimeoutSec(), TimeUnit.SECONDS)
				.operationTimeout(pulsarProperties.getOperationTimeoutSec(), TimeUnit.SECONDS)
				.startingBackoffInterval(pulsarProperties.getStartingBackoffIntervalMs(), TimeUnit.MILLISECONDS)
				.maxBackoffInterval(pulsarProperties.getMaxBackoffIntervalSec(), TimeUnit.SECONDS);

		if (StringUtils.isNotBlank(pulsarProperties.getToken())) {
			maxBackoffInterval.authentication(AuthenticationFactory.token(pulsarProperties.getToken()));
		}

		return maxBackoffInterval.build();

	}
}
