package com.hanbang.e.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private ObjectMapper om;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 10 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String createToken(Long memberId) {
        Date date = new Date();
		Claims claims = Jwts.claims();
		claims.put("memberID", memberId);

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject("token")
						.setClaims(claims)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, ???????????? ?????? JWT ?????? ?????????.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, ????????? JWT token ?????????.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, ???????????? ?????? JWT ?????? ?????????.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, ????????? JWT ?????? ?????????.");
        }
        return false;
    }

	public Long getMemberIdFromToken(HttpServletRequest request) {
		String token = resolveToken(request);

		// ????????? ????????? ??????
		if (token == null)
			throw new IllegalArgumentException("????????? ???????????? ????????????.");

		// ????????? ????????? ????????? ???????????? ??????
		if (!validateToken(token))
			throw new IllegalArgumentException("????????? ???????????????.");

		// ????????? ?????? ??????????????? ????????? ??????
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		String strId = String.valueOf(claims.get("memberID"));
		Long memberId = Long.valueOf(strId);
		return memberId;
	}
}
