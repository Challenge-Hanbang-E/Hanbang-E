package com.hanbang.e.common.jwt;

import com.hanbang.e.member.dto.MemberCreateReq;
import com.hanbang.e.member.dto.MemberLoginReq;

import org.assertj.core.api.WithAssertions;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest implements WithAssertions {
	private final JwtUtil jwtUtil;

	@Autowired
	public JwtUtilTest(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Test
	public void createToken() {
		/* given - 데이터 준비 */
		MemberLoginReq memberLoginReq = new MemberLoginReq(
			"이메일", "비밀번호");
		MemberCreateReq memberCreateReq = new MemberCreateReq(
			"이메일1", "비밀번호1","주소1");

		/* token 생성 */
		String token1 = this.jwtUtil.createToken(1L);
		String token2 = this.jwtUtil.createToken(2L);

		/* then - 검증 */
		assertThat(token1).isNotNull();
		assertThat(token1).isNotEqualTo(token2).isNotNull();
	}
}
