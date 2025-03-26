package com.example.wallet_example1.balance.infrastructure.kafka;

import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.service.port.BalanceEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultBalanceEventPublisher implements BalanceEventPublisher {
    private final KafkaTemplate<String, BalanceEvent> kafkaTemplate;
    @Override
    public void publishEvent(String topic, String partitionKey, BalanceEvent balanceEvent) {
        kafkaTemplate.send(topic, partitionKey, balanceEvent);
    }
}
