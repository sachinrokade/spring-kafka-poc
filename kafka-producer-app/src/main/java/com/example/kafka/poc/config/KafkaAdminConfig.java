package com.example.kafka.poc.config;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaAdminConfig {

    private final KafkaAdmin kafkaAdmin;

    public KafkaAdminConfig(KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin.getConfigurationProperties());
    }
}
