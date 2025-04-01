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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @Test
    void 다건_전자지갑_이체() {
        Long balanceId1 = 0L;
        Long balanceId2 = 1L;
        Long balanceId3 = 2L;
        List<Long> eventIdList = new ArrayList<>();
        try {
            // given
            int transferCount = 5;
            balanceService.initializeBalance(balanceId1, balanceId1, "10000");
            balanceService.initializeBalance(balanceId2, balanceId2, "20000");
            balanceService.initializeBalance(balanceId3, balanceId3, "30000");
            ExecutorService executorService = Executors.newFixedThreadPool(transferCount);
            CountDownLatch latch = new CountDownLatch(transferCount);

            // when
            for(long i=0; i<transferCount; i++) {
                long finalI = i;
                executorService.execute(() -> {

                   Long senderBalanceId = finalI % 3;
                   Long receiverBalanceId = (finalI + 1) % 3;
                   Long eventId = finalI + (1234L);
                   Long amount = 1000 * (finalI+1);
                   eventIdList.add(eventId);
                   BalanceEvent balanceEvent = BalanceEvent.builder()
                            .eventId(eventId)
                            .senderBalanceId(senderBalanceId.toString())
                            .receiverBalanceId(receiverBalanceId.toString())
                            .balanceOperation(BalanceOperation.SUBTRACT)
                            .amount(amount.toString())
                            .senderId(senderBalanceId)
                            .receiverId(receiverBalanceId)
                            .build();
                   balanceService.publishBalanceEvent(balanceEvent);

                   latch.countDown();
               });
            }
            latch.await(5L, TimeUnit.SECONDS);
            Thread.sleep(5000L);

            // then
            Balance balance1 = balanceRepository.findByBalanceId(balanceId1);
            Balance balance2 = balanceRepository.findByBalanceId(balanceId2);
            Balance balance3 = balanceRepository.findByBalanceId(balanceId3);
            Assertions.assertEquals("8000", balance1.getAmount());
            Assertions.assertEquals("18000", balance2.getAmount());
            Assertions.assertEquals("34000", balance3.getAmount());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            balanceRepository.deleteByBalanceId(balanceId1);
            balanceRepository.deleteByBalanceId(balanceId2);
            balanceRepository.deleteByBalanceId(balanceId3);
            for(Long eventId : eventIdList) {
                balanceEventOutboxRepository.delete(eventId);
            }
        }
    }


}
