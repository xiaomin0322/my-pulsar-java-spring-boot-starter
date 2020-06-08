package io.github.majusko.pulsar.producer;

import java.util.Map;

import io.github.majusko.pulsar.collector.ProducerHolder;

public interface PulsarProducerFactory {
    Map<String, ProducerHolder> getTopics();
}
