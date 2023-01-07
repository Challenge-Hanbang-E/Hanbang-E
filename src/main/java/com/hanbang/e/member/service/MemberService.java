package com.hanbang.e.member.service;

import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberResp;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResp signup(MemberCreateReq memberCreateReq) {
        validateDuplicateMember(memberCreateReq);
        Member member = Member.of(memberCreateReq.getEmail(), memberCreateReq.getPassword(), memberCreateReq.getAddress());
        memberRepository.save(member);
        return MemberResp.of(member.getEmail(), member.getAddress());
    }

    private void validateDuplicateMember(MemberCreateReq memberCreateReq) {
        memberRepository.findByEmail(memberCreateReq.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
                });
    }
}