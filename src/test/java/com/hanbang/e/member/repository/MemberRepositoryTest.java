package com.hanbang.e.member.repository;

import com.hanbang.e.member.entity.Member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

        Member member1 = new Member("이메일","비밀번호","주소");
        memberRepository.save(member1);

        Member member2 = new Member("이메일1","비밀번호1","주소1");
        memberRepository.save(member2);
    }

    @Test
    @DisplayName("회원가입")
    public void createMemberTest() {
        /* given - 데이터 준비 */
        Member member = new Member("member1","비밀번호","주소");

        /* when - 테스트 실행 */
        Member member1 = memberRepository.save(member);
        Member result = memberRepository.findById(member1.getMemberId()).get();

        /* then - 검증 */
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getAddress()).isEqualTo(member.getAddress());
    }

    @Test
    @DisplayName("이메일 멤버찾기")
    public void findByEmailTest() {
        /* given - 데이터 준비 */

        /* when - 테스트 실행 */
        Member result1 = memberRepository.findByEmail("이메일").get();
        Member result2 = memberRepository.findByEmail("이메일1").get();

        /* then - 검증 */
        assertThat(result1.getEmail()).isEqualTo("이메일");
        assertThat(result2.getEmail()).isEqualTo("이메일1");
    }
}