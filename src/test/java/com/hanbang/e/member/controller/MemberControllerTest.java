package com.hanbang.e.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberLoginReq;
import com.hanbang.e.member.entity.Member;
import com.hanbang.e.member.repository.MemberRepository;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @Autowired
    TestRestTemplate rt;

    @Autowired
    private MemberRepository memberRepository;

    private static ObjectMapper om;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    public void setup() {

        Member member = new Member("이메일","비밀번호","주소");
        memberRepository.save(member);
    }

    @AfterEach
    public void clear() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void signupTest() throws JsonProcessingException {
        /* given - 데이터 준비 */
        MemberCreateReq memberCreateReq = new MemberCreateReq(
                "hanghae@naver.com","Hanghae11!@","부산시");
        String body = om.writeValueAsString(memberCreateReq);

        /* when - 테스트 실행 */
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/member/signup", HttpMethod.POST, request, String.class);
        DocumentContext dc = JsonPath.parse(response.getBody());
        String result = dc.read("$.result");
        String msg = dc.read("$.msg");

        /* then - 검증 */
        assertThat(result).isEqualTo("success");
        assertThat(msg).isEqualTo("회원가입 완료");
    }

    @Test
    @DisplayName("로그인 성공")
    public void loginSuccessTest() throws JsonProcessingException {
        /* given - 데이터 준비 */
        MemberLoginReq memberLoginReq1 = new MemberLoginReq("이메일" ,"비밀번호");
        String body1 = om.writeValueAsString(memberLoginReq1);

        /* when - 테스트 실행 */
        HttpEntity<String> request1 = new HttpEntity<>(body1, headers);
        ResponseEntity<String> response1 = rt.exchange("/api/member/login", HttpMethod.POST, request1, String.class);
        DocumentContext dc1 = JsonPath.parse(response1.getBody());
        String result1 = dc1.read("$.result");
        String msg1 = dc1.read("$.msg");

        /* then - 검증 */
        assertThat(result1).isEqualTo("success");
        assertThat(msg1).isEqualTo("로그인 완료");
    }

    @Test
    @DisplayName("로그인 실패 (등록된 사용자가 없습니다)")
    public void loginFailTest() throws JsonProcessingException {
        /* given - 데이터 준비 */
        MemberLoginReq memberLoginReq = new MemberLoginReq("이메일1" ,"비밀번호");
        String body = om.writeValueAsString(memberLoginReq);

        /* when - 테스트 실행 */
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/member/login", HttpMethod.POST, request, String.class);
        DocumentContext dc = JsonPath.parse(response.getBody());
        String result = dc.read("$.result");
        String msg = dc.read("$.msg");

        /* then - 검증 */
        assertThat(result).isEqualTo("fail");
        assertThat(msg).isEqualTo("등록된 사용자가 없습니다.");
    }

    @Test
    @DisplayName("로그인 실패 (비밀번호가 일치하지 않습니다.)")
    public void loginFail1Test() throws JsonProcessingException {
        /* given - 데이터 준비 */
        MemberLoginReq memberLoginReq = new MemberLoginReq("이메일" ,"비밀번호1");
        String body = om.writeValueAsString(memberLoginReq);

        /* when - 테스트 실행 */
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/member/login", HttpMethod.POST, request, String.class);
        DocumentContext dc = JsonPath.parse(response.getBody());
        String result = dc.read("$.result");
        String msg = dc.read("$.msg");

        /* then - 검증 */
        assertThat(result).isEqualTo("fail");
        assertThat(msg).isEqualTo("비밀번호가 일치하지 않습니다.");
    }
}