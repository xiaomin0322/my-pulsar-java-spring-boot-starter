package io.github.majusko.pulsar.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ProducerConfigManage {

	@Autowired
	private Environment environment;

	private static String prefix = "pulsar.producer.%s.";

	public static final Map<String, Map<String, Object>> CONFIG_MAP = new ConcurrentHashMap<>();

	public Map<String, Object> getConfig(String topicName) {
		if (StringUtils.isBlank(topicName)) {
			return null;
		}
		String key = String.format(prefix, topicName);
		Map<String, Object> map = CONFIG_MAP.get(key);
		if (!CollectionUtils.isEmpty(map)) {
			return map;
		}
		map = new HashMap<>();

		for (String s : getKeySet()) {
			String sk = key + s;
			String val = environment.getProperty(sk);
			if (StringUtils.isBlank(val)) {
				continue;
			}
			map.put(s, val);
		}
		CONFIG_MAP.put(key, map);
		return map;
	}

	public Set<String> getKeySet() {
		ProducerConfigurationDataExt configurationData = new ProducerConfigurationDataExt();
		return configurationData.toMap().keySet();
	}

}
