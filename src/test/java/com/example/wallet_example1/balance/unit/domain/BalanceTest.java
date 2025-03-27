package com.example.wallet_example1.balance.unit.domain;

import com.example.wallet_example1.balance.domain.Balance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BalanceTest {
    @Test
    void 잔고_차감() {
        // given
        Balance balance = Balance.builder()
                .memberId(1L)
                .balanceId("110352111111")
                .amount("10000")
                .build();
        // when
        balance.subtractAmount("10000");
        // then
        String amount = balance.getAmount();
        Assertions.assertEquals("0", amount);
    }

    @Test
    void 잔고_증가() {
        // given
        Balance balance = Balance.builder()
                .memberId(1L)
                .balanceId("110352111111")
                .amount("10000")
                .build();
        // when
        balance.addAmount("5000");
        // then
        String amount = balance.getAmount();
        Assertions.assertEquals("15000", amount);
    }

    @Test
    void 잔고_부족() {
        // given
        Balance balance = Balance.builder()
                .memberId(1L)
                .balanceId("110352111111")
                .amount("10000")
                .build();
        // when then
        Assertions.assertThrows(IllegalArgumentException.class , () -> balance.subtractAmount("300000"));

    }
}
