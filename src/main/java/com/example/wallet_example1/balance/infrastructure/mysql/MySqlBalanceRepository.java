package com.example.wallet_example1.balance.infrastructure.mysql;

import com.example.wallet_example1.balance.domain.Balance;
import com.example.wallet_example1.balance.infrastructure.entity.BalanceEntity;
import com.example.wallet_example1.balance.service.port.BalanceRepository;
import com.example.wallet_example1.common.aop.sharding.EnableSharding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlBalanceRepository implements BalanceRepository {
    private final BalanceJpaRepository jpaRepository;
    @Override
    @EnableSharding
    public Balance save(Long balanceId, Balance balance) {
        BalanceEntity entity = balance.toEntity();
        BalanceEntity save = jpaRepository.save(entity);
        return Balance.from(save);
    }

    @Override
    @EnableSharding
    public Balance findByBalanceId(Long balanceId) {

        BalanceEntity entity = jpaRepository.findById(balanceId.toString())
                .orElseThrow(() -> new IllegalArgumentException("Balance Not Found"));
        return Balance.from(entity);
    }

}
