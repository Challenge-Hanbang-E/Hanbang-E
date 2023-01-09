package com.hanbang.e.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanbang.e.member.dto.MemberCreateReq;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.BeforeAll;
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

    private static ObjectMapper om;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

        /* then - 검증 */
        DocumentContext dc = JsonPath.parse(response.getBody());
        String result = dc.read("$.result");
        String msg = dc.read("$.msg");

        assertThat(result).isEqualTo("success");
        assertThat(msg).isEqualTo("회원가입 완료");
    }
}