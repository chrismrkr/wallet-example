package com.example.wallet_example1.balance.service.port;

import com.example.wallet_example1.balance.domain.BalanceEvent;

public interface BalanceEventPublisher {
    void publishEvent(String topic, String partitionKey, BalanceEvent balanceEvent);
}
