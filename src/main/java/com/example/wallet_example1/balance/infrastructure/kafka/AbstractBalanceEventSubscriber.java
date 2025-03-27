package com.example.wallet_example1.balance.infrastructure.kafka;


import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.service.port.BalanceEventSubscriber;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

public abstract class AbstractBalanceEventSubscriber implements BalanceEventSubscriber {
    @Override
    @KafkaListener(
            topics = {"#{balanceEventStreamInfo.topicName}"},
            groupId = "#{balanceEventStreamInfo.groupId}",
            containerFactory = "concurrentBalanceEventConsumerFactory"
    )
    public void subscribeEvent(ConsumerRecord<String, BalanceEvent> record, Acknowledgment ack) {
        try {
            BalanceEvent event = record.value();
            this.doTransaction(event);
            ack.acknowledge();
            this.doNextTransaction(event);
        } catch (Exception e) {
            throw e;
        }
    }
    protected void doTransaction(BalanceEvent balanceEvent) {}
    protected void doNextTransaction(BalanceEvent balanceEvent) {}
}
