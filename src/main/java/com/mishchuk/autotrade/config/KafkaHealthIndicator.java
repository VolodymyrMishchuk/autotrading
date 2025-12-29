package com.mishchuk.autotrade.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaHealthIndicator implements HealthIndicator {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Override
    public Health health() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        try (AdminClient client = AdminClient.create(props)) {
            client.listTopics(new ListTopicsOptions().timeoutMs(3000)).names().get();
            return Health.up().withDetail("bootstrap", bootstrapServers).build();
        } catch (Exception e) {
            return Health.down(e).withDetail("bootstrap", bootstrapServers).build();
        }
    }
}
