package com.example.wallet_example1.balance.domain;

import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceEvent {
    private Long eventId;
    private String balanceId;
    private BalanceOperation balanceOperation;
    private String amount;
    @Builder
    private BalanceEvent(Long eventId, String balanceId, BalanceOperation balanceOperation, String amount) {
        this.eventId = eventId;
        this.balanceId = balanceId;
        this.balanceOperation = balanceOperation;
        this.amount = amount;
    }
}
