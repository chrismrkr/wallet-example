package com.example.wallet_example1.balance.infrastructure.entity;

import com.example.wallet_example1.balance.domain.enums.BalanceEventStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceEventOutboxEntity {
    @Id
    private Long eventId;
    private Long senderId;
    private Long receiverId;
    private String amount;
    @Enumerated(value = EnumType.STRING)
    private BalanceEventStatus status;

    @Builder
    private BalanceEventOutboxEntity(Long eventId, Long senderId, Long receiverId, String amount, BalanceEventStatus status) {
        this.eventId = eventId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }
}
