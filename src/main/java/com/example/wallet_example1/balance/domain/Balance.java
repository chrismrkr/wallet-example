package com.example.wallet_example1.balance.domain;

import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
import com.example.wallet_example1.balance.infrastructure.entity.BalanceEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance {
    private Long memberId;
    private String balanceId;
    private String amount;

    public void subtractAmount(String amt) {
        if (amt == null) {
            throw new IllegalArgumentException("add must not be Null");
        }

        try {
            BigDecimal num1 = new BigDecimal(this.amount);
            BigDecimal num2 = new BigDecimal(amt);
            BigDecimal result;
            result = num1.subtract(num2);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Not Enough Balance");
            }
            this.amount = result.stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Number Format String");
        }
    }

    public void addAmount(String amt) {
        if (amt == null) {
            throw new IllegalArgumentException("add must not be Null");
        }
        try {
            BigDecimal num1 = new BigDecimal(this.amount);
            BigDecimal num2 = new BigDecimal(amt);
            BigDecimal result;
            result = num1.add(num2);
            this.amount = result.stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Number Format String");
        }
    }

    @Builder
    private Balance(Long memberId, String balanceId, String amount) {
        this.memberId = memberId;
        this.balanceId = balanceId;
        this.amount = amount;
    }

    public BalanceEntity toEntity() {
        return BalanceEntity.builder()
                .balanceId(this.balanceId)
                .memberId(this.memberId)
                .amount(this.amount)
                .build();
    }
    public static Balance from(BalanceEntity entity) {
        return Balance.builder()
                .balanceId(entity.getBalanceId())
                .memberId(entity.getMemberId())
                .amount(entity.getAmount())
                .build();
    }
}
