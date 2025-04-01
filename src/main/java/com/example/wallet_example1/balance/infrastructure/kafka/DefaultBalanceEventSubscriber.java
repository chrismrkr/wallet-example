package com.example.wallet_example1.balance.infrastructure.kafka;

import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.infrastructure.kafka.handler.BalanceEventHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Setter
public class DefaultBalanceEventSubscriber extends AbstractBalanceEventSubscriber {
    private BalanceEventHandler balanceDecreaseEventHandler;
    private BalanceEventHandler balanceDecreaseEventPostHandler;
    private BalanceEventHandler balanceIncreaseEventHandler;
    private BalanceEventHandler balanceIncreaseEventPostHandler;

    @Override
    @Transactional
    public void doTransaction(BalanceEvent balanceEvent) {
        switch (balanceEvent.getBalanceOperation()) {
            case SUBTRACT -> {
                balanceDecreaseEventHandler.handle(balanceEvent);
            }
            case ADD -> {
                balanceIncreaseEventHandler.handle(balanceEvent);
            }
            default -> throw new IllegalArgumentException("Not Supported Balance Operation");
        }
    }

    @Override
    @Transactional
    public void doNextTransaction(BalanceEvent balanceEvent) {
        switch (balanceEvent.getBalanceOperation()) {
            case SUBTRACT -> balanceDecreaseEventPostHandler.handle(balanceEvent);
            case ADD -> balanceIncreaseEventPostHandler.handle(balanceEvent);
            default -> throw new IllegalArgumentException("Not Supported Balance Operation");
        }
    }
}
