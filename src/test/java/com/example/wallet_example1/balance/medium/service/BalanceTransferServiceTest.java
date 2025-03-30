package com.example.wallet_example1.balance.medium.service;

import com.example.wallet_example1.balance.controller.port.BalanceService;
import com.example.wallet_example1.balance.domain.Balance;
import com.example.wallet_example1.balance.domain.BalanceEvent;
import com.example.wallet_example1.balance.domain.BalanceEventOutbox;
import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
import com.example.wallet_example1.balance.service.port.BalanceEventOutboxRepository;
import com.example.wallet_example1.balance.service.port.BalanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BalanceTransferServiceTest {
    @Autowired
    BalanceService balanceService;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    BalanceEventOutboxRepository balanceEventOutboxRepository;

    @Test
    void 단건_계좌_이체() {
        Long balanceId1 = 1L;
        Long balanceId2 = 2L;
        Long eventId = 123123L;
        try {
            // given : 계좌 생성
            balanceService.initializeBalance(1L, 1L, "10000");
            balanceService.initializeBalance(2L, 2L, "30000");

            // when : 계좌 이체 ( 1 -> 2 5000원 이체)
            BalanceEvent balanceEvent = BalanceEvent.builder()
                    .eventId(eventId)
                    .senderBalanceId(balanceId1.toString())
                    .receiverBalanceId(balanceId2.toString())
                    .balanceOperation(BalanceOperation.SUBTRACT)
                    .amount("7000")
                    .senderId(balanceId1)
                    .receiverId(balanceId2)
                    .build();
            balanceService.publishBalanceEvent(balanceEvent);
            Thread.sleep(3000L);

            // then
            Balance balance1 = balanceRepository.findByBalanceId(balanceId1);
            Balance balance2 = balanceRepository.findByBalanceId(balanceId2);
            Assertions.assertEquals("3000",balance1.getAmount());
            Assertions.assertEquals("37000", balance2.getAmount());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            balanceRepository.deleteByBalanceId(balanceId1);
            balanceRepository.deleteByBalanceId(balanceId2);
            balanceEventOutboxRepository.delete(eventId);
        }
    }


}
