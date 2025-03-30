package com.example.wallet_example1.balance.domain;

import com.example.wallet_example1.balance.domain.enums.BalanceEventStatus;
import com.example.wallet_example1.balance.domain.enums.BalanceOperation;
import com.example.wallet_example1.balance.infrastructure.entity.BalanceEventOutboxEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceEventOutbox {
    private Long eventId;
    private Long senderId;
    private Long receiverId;
    private String amount;
    private BalanceEventStatus status;

    public void changeStatus(BalanceEventStatus status) {
        this.status = status;
    }
    @Builder
    private BalanceEventOutbox(Long eventId, Long senderId, Long receiverId, String amount, BalanceEventStatus status) {
        this.eventId = eventId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }

    public static BalanceEventOutbox from(BalanceEventOutboxEntity entity) {
        return BalanceEventOutbox.builder()
                .eventId(entity.getEventId())
                .senderId(entity.getSenderId())
                .receiverId(entity.getReceiverId())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .build();
    }

    public BalanceEventOutboxEntity toEntity() {
        return BalanceEventOutboxEntity.builder()
                .eventId(this.getEventId())
                .senderId(this.getSenderId())
                .receiverId(this.getReceiverId())
                .amount(this.getAmount())
                .status(this.getStatus())
                .build();
    }
}
