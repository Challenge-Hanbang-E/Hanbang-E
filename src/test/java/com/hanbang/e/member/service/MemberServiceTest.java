package com.hanbang.e.member.service;

import com.hanbang.e.member.dto.RequestCreateMember;
import com.hanbang.e.member.dto.ResponseMember;
import com.hanbang.e.member.repository.MemberRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    public void signupTest() {
        /* given - 데이터 준비 */
        RequestCreateMember requestCreateMember = RequestCreateMember.builder()
                .email("hanghae12@naver.com")
                .password("Hanghae123!@")
                .address("부산시")
                .build();

        memberService.signup(requestCreateMember);

        /* 가짜객체의 행동 정의 */
        when(memberRepository.save(any())).thenReturn(requestCreateMember.toEntity());

        /* when - 테스트 실행 */
        ResponseMember responseMember = memberService.signup(requestCreateMember);

        /* then - 검증 */
        assertThat(responseMember.getEmail()).isEqualTo(requestCreateMember.getEmail());
    }
}