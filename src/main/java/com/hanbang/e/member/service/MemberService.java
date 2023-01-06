package com.hanbang.e.member.service;

import com.hanbang.e.member.dto.RequestCreateMember;
import com.hanbang.e.member.dto.ResponseMember;
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
    public ResponseMember signup(RequestCreateMember requestCreateMember) {
        validateDuplicateMember(requestCreateMember);
        Member member = new Member(requestCreateMember.getEmail(), requestCreateMember.getPassword(), requestCreateMember.getAddress());
        memberRepository.save(member);
        return new ResponseMember(member.getEmail(), member.getAddress());
    }

    private void validateDuplicateMember(RequestCreateMember requestCreateMember) {
        memberRepository.findByEmail(requestCreateMember.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
                });
    }
}