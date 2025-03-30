package com.example.wallet_example1.balance.service.port;

import com.example.wallet_example1.balance.domain.Balance;
import com.example.wallet_example1.balance.domain.BalanceEvent;

public interface BalanceRepository {
    Balance save(Long balanceId, Balance balance);
    Balance findByBalanceId(Long balanceId);
    void deleteByBalanceId(Long balanceId);
}
