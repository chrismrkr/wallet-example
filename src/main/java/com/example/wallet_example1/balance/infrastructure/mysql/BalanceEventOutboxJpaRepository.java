package com.example.wallet_example1.balance.infrastructure.mysql;

import com.example.wallet_example1.balance.infrastructure.entity.BalanceEventOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceEventOutboxJpaRepository extends JpaRepository<BalanceEventOutboxEntity, Long> {
}
