package io.github.majusko.pulsar;

import java.util.Map;

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

import io.github.majusko.pulsar.util.ConfigurationDataUtils;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ComponentScan
@EnableConfigurationProperties(PulsarProperties.class)
@Slf4j
public class PulsarAutoConfiguration {

	private final PulsarProperties pulsarProperties;

	public PulsarAutoConfiguration(PulsarProperties pulsarProperties) {
		this.pulsarProperties = pulsarProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public PulsarClient pulsarClient() throws PulsarClientException {
		if(pulsarProperties == null) {
		   log.error("pulsarProperties is null");	
		   return null;
		}
		ClientBuilder maxBackoffInterval = PulsarClient.builder();
		Map<String, Object> map = ConfigurationDataUtils.toMap(pulsarProperties, PulsarProperties.class);
		maxBackoffInterval.loadConf(map);
		if (StringUtils.isNotBlank(pulsarProperties.getToken())) {
			maxBackoffInterval.authentication(AuthenticationFactory.token(pulsarProperties.getToken()));
		}
		return maxBackoffInterval.build();

	}
}
