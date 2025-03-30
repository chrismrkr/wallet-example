package com.example.wallet_example1.balance.infrastructure.kafka;


import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.exception.DuplicateBalanceEventException;
import com.example.wallet_example1.balance.service.port.BalanceEventSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
public abstract class AbstractBalanceEventSubscriber implements BalanceEventSubscriber {
    @Override
    @KafkaListener(
            topics = {"#{balanceEventStreamInfo.topicName}"},
            groupId = "#{balanceEventStreamInfo.groupId}",
            containerFactory = "concurrentBalanceEventConsumerFactory"
    )
    public void subscribeEvent(ConsumerRecord<String, BalanceEvent> record, Acknowledgment ack) {
        BalanceEvent event = record.value();
        try {
            this.doTransaction(event);
            ack.acknowledge();
        } catch (DuplicateBalanceEventException e) {
            ack.acknowledge();
            return;
        } catch (Exception e) {
            log.info("[ERROR] {}", e.getMessage());
            throw e;
        }

        this.doNextTransaction(event);
    }
    protected void doTransaction(BalanceEvent balanceEvent) {}
    protected void doNextTransaction(BalanceEvent balanceEvent) {}
}
