package io.github.majusko.pulsar.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="pulsar")
@Data
public class ConsumerCustomConfig {
	Map<String,ConsumerCustomDetailConfig> consumer;
}

