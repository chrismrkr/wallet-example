package com.example.wallet_example1.balance.service.port;

import com.example.wallet_example1.balance.domain.BalanceEventOutbox;

import java.util.Optional;

public interface BalanceEventOutboxRepository {
    BalanceEventOutbox save(Long eventId, BalanceEventOutbox balanceEventOutbox);
    Optional<BalanceEventOutbox> findByEventId(Long eventId);
    void delete(Long eventId);
}
