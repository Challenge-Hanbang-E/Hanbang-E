package com.hanbang.e.member.service;

import com.hanbang.e.common.jwt.JwtUtil;
import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberLoginReq;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원가입")
    public void signupTest() {
        /* given - 데이터 준비 */
        MemberCreateReq memberCreateReq = new MemberCreateReq(
                "hanghae@naver.com", "Hanghae1!@", "부산시");

        /* stub - 가짜 객체 행동 정의 */
        when(memberRepository.save(any())).thenReturn(memberCreateReq.toEntity());

        /* when - 테스트 실행 */
        memberService.signup(memberCreateReq);

        /* then - 검증 */
        assertThat(memberCreateReq.getEmail()).isEqualTo(memberCreateReq.getEmail());
        assertThat(memberCreateReq.getAddress()).isEqualTo(memberCreateReq.getAddress());
    }

    @Test
    @DisplayName("로그인")
    public void loginTest() {
        /* given - 데이터 준비 */
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Member member = new Member(
                "이메일", "비밀번호", "주소");
        MemberLoginReq memberLoginReq = new MemberLoginReq(
                "이메일", "비밀번호");

        /* stub - 가짜 객체 행동 정의 */
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of((memberLoginReq.toEntity())));

        /* when - 테스트 실행 */
        memberService.login(memberLoginReq, httpServletResponse);

        /* then - 검증 */
        assertThat(member.getEmail()).isEqualTo(memberLoginReq.getEmail());
        assertThat(member.getPassword()).isEqualTo(memberLoginReq.getPassword());
    }

    @Test
    @DisplayName("로그인실패 (등록된 사용자가 없습니다.)")
    public void loginFailTest() {
        /* given - 데이터 준비 */
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        MemberLoginReq memberLoginReq = new MemberLoginReq(
                "이메일1", "비밀번호");

        /* stub - 가짜 객체 행동 정의 */
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

        /* when & then - 테스트 실행 및 검증 */
        assertThatThrownBy(
                () -> {
                    memberService.login(memberLoginReq, httpServletResponse);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 사용자가 없습니다.");
    }

    @Test
    @DisplayName("로그인실패 (비밀번호가 일치하지 않습니다.)")
    public void loginFailTest1() {
        /* given - 데이터 준비 */
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        Member member = new Member(
                "이메일", "비밀번호", "주소");
        MemberLoginReq memberLoginReq = new MemberLoginReq(
                "이메일", "비밀번호1");

        /* stub - 가짜 객체 행동 정의 */
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of((member)));

        /* when & then - 테스트 실행 및 검증 */
        assertThatThrownBy(
                () -> {
                    memberService.login(memberLoginReq, httpServletResponse);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }
}
