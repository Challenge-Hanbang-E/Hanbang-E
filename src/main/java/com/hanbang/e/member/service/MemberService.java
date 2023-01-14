package com.hanbang.e.member.service;

import com.hanbang.e.common.jwt.JwtUtil;
import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberLoginReq;
import com.hanbang.e.member.dto.MemberResp;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public Member signup(MemberCreateReq memberCreateReq) {
        memberRepository.findByEmail(memberCreateReq.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
                });

        Member member = memberCreateReq.toEntity();
        memberRepository.save(member);
        return member;
    }

    @Transactional(readOnly = true)
    public void login(MemberLoginReq memberLoginReq, HttpServletResponse httpServletResponse) {
        Member member = memberRepository.findByEmail(memberLoginReq.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        if (!member.getPassword().equals(memberLoginReq.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getMemberId()));
    }
}