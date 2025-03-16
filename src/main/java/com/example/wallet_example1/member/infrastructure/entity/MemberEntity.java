package com.example.wallet_example1.member.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberEntity {
    @Id
    private Long memberId;
    private String name;

    @Builder
    private MemberEntity(Long memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }
}
