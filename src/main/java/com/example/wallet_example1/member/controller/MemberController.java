package com.example.wallet_example1.member.controller;

import com.example.wallet_example1.member.infrastructure.MemberJpaRepository;
import com.example.wallet_example1.member.service.MemberService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public String createMember(@RequestBody MemberCreateDto dto) {
        String member = memberService.createMember(dto.getId(), dto.getName());
        return member;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class MemberCreateDto {
        private Long id;
        private String name;
        public MemberCreateDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
