package com.example.wallet_example1.balance.infrastructure.mysql;

import com.example.wallet_example1.balance.domain.BalanceEventOutbox;
import com.example.wallet_example1.balance.infrastructure.entity.BalanceEventOutboxEntity;
import com.example.wallet_example1.balance.service.port.BalanceEventOutboxRepository;
import com.example.wallet_example1.common.aop.sharding.EnableSharding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MysqlBalanceEventOutboxRepository implements BalanceEventOutboxRepository {
    private final BalanceEventOutboxJpaRepository jpaRepository;
    @Override
    @EnableSharding
    public BalanceEventOutbox save(Long eventId, BalanceEventOutbox balanceEventOutbox) {
        BalanceEventOutboxEntity entity = balanceEventOutbox.toEntity();
        BalanceEventOutboxEntity save = jpaRepository.save(entity);
        return BalanceEventOutbox.from(save);
    }

    @Override
    @EnableSharding
    public Optional<BalanceEventOutbox> findByEventId(Long eventId) {
         return jpaRepository.findById(eventId)
                .stream().map(BalanceEventOutbox::from)
                .findAny();

    }

    @Override
    @EnableSharding
    public void delete(Long eventId) {
        jpaRepository.deleteById(eventId);
    }
}
