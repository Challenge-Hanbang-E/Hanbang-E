package com.hanbang.e.member.repository;

import com.hanbang.e.member.entity.Member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {

        Member member1 = Member.builder()
                .email("hanghae1@naver.com")
                .password("Hanghae11!@")
                .address("부산시")
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .email("hanghae2@naver.com")
                .password("Hanghae12!@#")
                .address("부산시")
                .build();
        memberRepository.save(member2);
    }

    @AfterEach
    public void clear() {
        memberRepository.deleteAll();
    }

    @Test
    public void createMemberTest() {
        /* given - 데이터 준비 */
        String email = "hanghae12@naver.com";
        String password = "Hanghae123!@";
        String address = "부산시";
        Member member = Member.builder()
                .email(email)
                .password(password)
                .address(address)
                .build();

        /* when - 테스트 실행 */
        Member member1 = memberRepository.save(member);
        Member result = memberRepository.findById(member1.getMemberId()).get();

        /* then - 검증 */
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getAddress()).isEqualTo(member.getAddress());
    }
}