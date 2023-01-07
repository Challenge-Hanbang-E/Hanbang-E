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

        Member member1 = new Member("hanghae1@naver.com","Hanghae1!@","부산시 동래구");
        memberRepository.save(member1);

        Member member2 = new Member("hanghae2@naver.com","Hanghae2!@","부산시 부산진구");
        memberRepository.save(member2);
    }

    @AfterEach
    public void clear() {
        memberRepository.deleteAll();
    }

    @Test
    public void createMemberTest() {
        /* given - 데이터 준비 */
        Member member = new Member("hanghae1@naver.com","Hanghae1!@","부산시 동래구");

        /* when - 테스트 실행 */
        Member member1 = memberRepository.save(member);
        Member result = memberRepository.findById(member1.getMemberId()).get();

        /* then - 검증 */
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getAddress()).isEqualTo(member.getAddress());
    }
}