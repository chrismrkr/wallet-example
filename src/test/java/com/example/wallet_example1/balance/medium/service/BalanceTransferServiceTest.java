package com.example.wallet_example1.balance.medium.service;

import com.example.wallet_example1.balance.controller.port.BalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BalanceTransferServiceTest {
    @Autowired
    BalanceService balanceService;

    @Test
    void 단건_계좌_이체() {
        // given : 계좌 생성
        balanceService.initializeBalance(1L, 1L, "10000");
        balanceService.initializeBalance(2L, 2L, "30000");

        // when : 계좌 이체 ( 1 -> 2 5000원 이체)
    }
}
