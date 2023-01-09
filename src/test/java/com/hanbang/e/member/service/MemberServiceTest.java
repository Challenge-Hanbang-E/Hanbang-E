package com.hanbang.e.member.service;

import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberLoginReq;
import com.hanbang.e.member.dto.MemberResp;

import com.hanbang.e.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

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
        assertThat(memberResp.getAddress()).isEqualTo(memberCreateReq.getAddress());
    }

    @Test
    @DisplayName("로그인")
    public void loginTest() {
        /* given - 데이터 준비 */
        MemberCreateReq memberCreateReq = new MemberCreateReq(
                "이메일", "비밀번호", "주소");
        MemberLoginReq memberLoginReq = new MemberLoginReq(
                "이메일", "비밀번호");
        /* when - 테스트 실행 */
        memberService.signup(memberCreateReq);

        /* then - 검증 */
        assertThat(memberCreateReq.getEmail()).isEqualTo(memberLoginReq.getEmail());
        assertThat(memberCreateReq.getPassword()).isEqualTo(memberLoginReq.getPassword());
    }
}