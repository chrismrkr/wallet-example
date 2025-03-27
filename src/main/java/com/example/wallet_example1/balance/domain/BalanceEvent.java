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
    private String senderBalanceId;
    private String receiverBalanceId;
    private BalanceOperation balanceOperation;
    private String amount;

    private Long senderId;
    private Long receiverId;
    @Builder
    private BalanceEvent(Long eventId, String senderBalanceId, String receiverBalanceId, BalanceOperation balanceOperation, String amount,
                        Long senderId, Long receiverId) {
        this.eventId = eventId;
        this.senderBalanceId = senderBalanceId;
        this.receiverBalanceId = receiverBalanceId;
        this.balanceOperation = balanceOperation;
        this.amount = amount;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
