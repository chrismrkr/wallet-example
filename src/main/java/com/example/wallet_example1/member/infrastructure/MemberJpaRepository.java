package com.example.wallet_example1.member.infrastructure;

import com.example.wallet_example1.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
}
