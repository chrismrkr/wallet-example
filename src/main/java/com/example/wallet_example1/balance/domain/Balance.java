package com.example.wallet_example1.balance.domain;

import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
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

    public void addAmount(String amt, BalanceOperation operation) {
        if (amt == null) {
            throw new IllegalArgumentException("add must not be Null");
        }
        try {
            BigDecimal num1 = new BigDecimal(this.amount);
            BigDecimal num2 = new BigDecimal(amt);
            BigDecimal result;
            switch (operation) {
                case ADD -> result = num1.add(num2);
                case SUBTRACT -> {
                    result = num1.subtract(num2);
                    if (result.compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException("Not Enough Balance");
                    }
                }
                default -> throw new IllegalArgumentException("Not Supported Operation");
            }
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
}
