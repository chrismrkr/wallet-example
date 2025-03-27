package com.example.wallet_example1.balance.infrastructure.kafka.handler;

import com.example.wallet_example1.balance.domain.BalanceEvent;

@FunctionalInterface
public interface BalanceEventHandler {
    void handle(BalanceEvent balanceEvent);
}
