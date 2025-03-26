package com.example.wallet_example1.balance.service.port;

import com.example.wallet_example1.balance.domain.BalanceEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.Record;
import org.springframework.kafka.support.Acknowledgment;

public interface BalanceEventSubscriber {
    void subscribeEvent(ConsumerRecord<String, BalanceEvent> record, Acknowledgment ack);
}
