package com.example.wallet_example1.balance.service;

import com.example.wallet_example1.balance.controller.port.BalanceService;
import com.example.wallet_example1.balance.domain.Balance;
import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
import com.example.wallet_example1.balance.infrastructure.kafka.DefaultBalanceEventSubscriber;
import com.example.wallet_example1.balance.service.port.BalanceEventPublisher;
import com.example.wallet_example1.balance.service.port.BalanceEventSubscriber;
import com.example.wallet_example1.balance.service.port.BalanceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository balanceRepository;
    private final BalanceEventPublisher balanceEventPublisher;
    private final BalanceEventSubscriber balanceEventSubscriber;
    private final Environment environment;
    @Override
    public void publishBalanceEvent(BalanceEvent balanceEvent) {
        balanceEventPublisher.publishEvent(
                environment.getProperty("spring.kafka.topic.balance.topic-name"), balanceEvent.getSenderBalanceId(),
                balanceEvent);
    }

    @PostConstruct
    void setBalanceEventHandler() {
        if(balanceEventSubscriber instanceof DefaultBalanceEventSubscriber) {
            ((DefaultBalanceEventSubscriber) balanceEventSubscriber).setBalanceDecreaseEventHandler(
                    balanceEvent -> {
                        Long balanceId = Long.parseLong(balanceEvent.getSenderBalanceId());
                        Balance balance = balanceRepository.findByBalanceId(balanceId);
                        balance.subtractAmount(balanceEvent.getAmount());
                        balanceRepository.save(balanceId, balance);

                        // TODO. Outbox 테이블 INSERT
                    });

            ((DefaultBalanceEventSubscriber) balanceEventSubscriber).setBalanceDecreaseEventPostHandler(
                    balanceEvent -> {
                        BalanceEvent newEvent = BalanceEvent.builder()
                                .eventId(balanceEvent.getEventId())
                                .senderBalanceId(balanceEvent.getSenderBalanceId())
                                .receiverBalanceId(balanceEvent.getReceiverBalanceId())
                                .balanceOperation(BalanceOperation.ADD)
                                .amount(balanceEvent.getAmount())
                                .senderId(balanceEvent.getSenderId())
                                .receiverId(balanceEvent.getReceiverId())
                                .build();
                        balanceEventPublisher.publishEvent(
                                environment.getProperty("spring.kafka.topic.balance.topic-name"),
                                balanceEvent.getReceiverBalanceId(),
                                newEvent);
                    });

            ((DefaultBalanceEventSubscriber) balanceEventSubscriber).setBalanceIncreaseEventHandler(
                    balanceEvent -> {
                        Long balanceId = Long.parseLong(balanceEvent.getReceiverBalanceId());
                        Balance balance = balanceRepository.findByBalanceId(balanceId);
                        balance.addAmount(balanceEvent.getAmount());
                        balanceRepository.save(balanceId, balance);

                        // TODO. Outbox 테이블 Update
                    });

            ((DefaultBalanceEventSubscriber) balanceEventSubscriber).setBalanceIncreaseEventPostHandler(
                    balanceEvent -> {
                        log.info("[Balance Transfer Success] From {} -> To {}, Amount: {}",
                                balanceEvent.getSenderBalanceId(),
                                balanceEvent.getReceiverBalanceId(),
                                balanceEvent.getAmount());
                    });
        }
    }

    @Override
    public void initializeBalance(Long memberId, Long balanceId, String amount) {
        Balance balance = Balance.builder()
                .memberId(memberId)
                .balanceId(balanceId.toString())
                .amount(amount)
                .build();
        balanceRepository.save(balanceId, balance);
    }
}
