package com.app.orderproductservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.record.CompressionType;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.sasl.username:}")
    private String saslUsername;

    @Value("${spring.kafka.properties.sasl.password:}")
    private String saslPassword;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        
        // Basic configuration
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // SASL/SSL Configuration
        if (saslUsername != null && !saslUsername.isEmpty()) {
            configProps.put("security.protocol", SecurityProtocol.SASL_SSL.name());
            configProps.put("sasl.mechanism", "PLAIN");
            configProps.put("sasl.jaas.config", PlainLoginModule.class.getName() + 
                " required username=\"" + saslUsername + "\" password=\"" + saslPassword + "\";");
        }

        // Retry and reliability configuration
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.GZIP.name);

        // Timeout configurations
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 5000);  // 5 seconds timeout for a single message send
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3000);   // 3 seconds request timeout
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);      // 100 ms backoff between retries

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
} 