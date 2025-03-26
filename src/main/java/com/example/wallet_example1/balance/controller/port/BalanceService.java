package com.example.wallet_example1.balance.controller.port;

import com.example.wallet_example1.balance.domain.BalanceEvent;

public interface BalanceService {
    void publishBalanceEvent(BalanceEvent balanceEvent);
    void subscribeBalanceEvent(BalanceEvent balanceEvent);
}
