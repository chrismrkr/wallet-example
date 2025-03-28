package com.example.wallet_example1.member.service;

import com.example.wallet_example1.common.aop.sharding.EnableSharding;
import com.example.wallet_example1.common.dataSource.impl.ThreadLocalShardKeyContextHolder;
import com.example.wallet_example1.member.infrastructure.MemberJpaRepository;
import com.example.wallet_example1.member.infrastructure.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberJpaRepository memberJpaRepository;

    @EnableSharding
    public String createMember(Long userId, String name) {
        MemberEntity build = MemberEntity.builder()
                .memberId(userId)
                .name(name)
                .build();
        memberJpaRepository.save(build);
        return "ok";
    }
}
