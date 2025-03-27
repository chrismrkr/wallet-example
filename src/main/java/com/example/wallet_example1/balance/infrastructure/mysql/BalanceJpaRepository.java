package com.example.wallet_example1.balance.infrastructure.mysql;

import com.example.wallet_example1.balance.infrastructure.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceJpaRepository extends JpaRepository<BalanceEntity, String> {
}
