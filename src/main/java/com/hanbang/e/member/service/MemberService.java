package com.hanbang.e.member.service;

import com.hanbang.e.common.annotation.distributeLock.DistributeLock;
import com.hanbang.e.common.jwt.JwtUtil;
import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberLoginReq;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static com.hanbang.e.common.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final JwtUtil jwtUtil;

//    @DistributeLock(key = "#member")
    @Transactional
    public void signup(MemberCreateReq memberCreateReq) {
        memberRepository.findByEmail(memberCreateReq.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException(OVERLAP_EMAIL_MSG.getMsg());
                });

        Member member = memberCreateReq.toEntity();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void login(MemberLoginReq memberLoginReq, HttpServletResponse httpServletResponse) {
        Member member = memberRepository.findByEmail(memberLoginReq.getEmail()).orElseThrow(
                () -> new IllegalArgumentException(NOT_FOUND_MEMBER_MSG.getMsg())
        );
        if (!member.getPassword().equals(memberLoginReq.getPassword())) {
            throw new IllegalArgumentException(NOT_MATCH_PASSWORD_MSG.getMsg());
        }
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getMemberId()));
    }
}
