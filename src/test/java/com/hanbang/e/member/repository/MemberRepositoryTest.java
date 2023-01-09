package com.hanbang.e.member.repository;

import com.hanbang.e.member.entity.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
    public void createMemberTest() {
        /* given - 데이터 준비 */
        Member member = new Member("이메일","비밀번호","주소");

        /* when - 테스트 실행 */
        Member member1 = memberRepository.save(member);
        Member result = memberRepository.findById(member1.getMemberId()).get();

        /* then - 검증 */
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getAddress()).isEqualTo(member.getAddress());
    }
}