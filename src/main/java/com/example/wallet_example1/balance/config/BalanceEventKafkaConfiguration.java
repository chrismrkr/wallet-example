package com.example.wallet_example1.balance.config;

import com.example.wallet_example1.balance.domain.BalanceEvent;
import lombok.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BalanceEventKafkaConfiguration {
    private final Environment env;
    @Bean(name = "balanceEventProducer")
    public KafkaTemplate<String, BalanceEvent> balanceEventProducer() {
        return new KafkaTemplate<>(balanceEventProducerFactory());
    }
    @Bean
    public ProducerFactory<String, BalanceEvent> balanceEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16KB 배치 크기
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(
                configProps
        );
    }

    @Bean(name = "concurrentBalanceEventConsumerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, BalanceEvent> concurrentBalanceEventConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BalanceEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(balanceEventConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, BalanceEvent> balanceEventConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // manual commit
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // Key 역직렬화
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, jsonDeserializer()); // Value 역직렬화
        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                jsonDeserializer()
        );
    }

    @Bean
    public JsonDeserializer<BalanceEvent> jsonDeserializer() {
        JsonDeserializer<BalanceEvent> jsonDeserializer = new JsonDeserializer<>(BalanceEvent.class);
        jsonDeserializer.addTrustedPackages("*");
        return jsonDeserializer;
    }

    @Bean(name = "balanceEventStreamInfo")
    public ConsumerTopicGroupInfo balanceEventStreamInfo() {
        String topicName = env.getProperty("spring.kafka.topic.balance.topic-name");
        String groupId = env.getProperty("spring.kafka.topic.balance.group-id");
        return new ConsumerTopicGroupInfo(topicName, groupId);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConsumerTopicGroupInfo {
        private String topicName;
        private String groupId;
    }
}
