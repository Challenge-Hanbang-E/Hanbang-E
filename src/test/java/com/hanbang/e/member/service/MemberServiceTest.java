package com.hanbang.e.member.service;

import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberResp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    public void signupTest() {
        /* given - 데이터 준비 */
        MemberCreateReq memberCreateReq = new MemberCreateReq(
                "hanghae1@naver.com","Hanghae1!@","부산시 동래구");

        /* when - 테스트 실행 */
        MemberResp memberResp = memberService.signup(memberCreateReq);

        /* then - 검증 */
        assertThat(memberResp.getEmail()).isEqualTo(memberCreateReq.getEmail());
    }
}