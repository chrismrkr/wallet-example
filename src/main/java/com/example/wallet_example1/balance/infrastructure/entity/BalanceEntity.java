package com.example.wallet_example1.balance.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceEntity {
    @Id
    private String balanceId;
    private Long memberId;
    private String amount;

    @Builder
    private BalanceEntity(String balanceId, Long memberId, String amount) {
        this.balanceId = balanceId;
        this.memberId = memberId;
        this.amount = amount;
    }
}
