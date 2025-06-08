package com.ssafy.kiriya.security.util.jwt;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssafy.kiriya.model.dto.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {
    private final SecretKey key;
    
    public JWTUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.debug("jwt secret key: {}", key.getEncoded());
    }

    @Value("${ssafy.jwt.access-expmin}")
    private long accessExpMin;

    @Value("${ssafy.jwt.refresh-expmin}")
    private long refreshExpMin;

    public String createAccessToken(Member member) {
        return create("accessToken", accessExpMin,
            Map.of(
                "email", member.getEmail(),
                "name",  member.getName(),
                "role",  member.getRole().getRoleName()
            )
        );
    }

    public String createRefreshToken(Member member) {
        return create("refreshToken", refreshExpMin,
            Map.of("email", member.getEmail())
        );
    }

    // =====================================================
    // 토큰 생성: subject, 만료기간, 클레임맵을 받아 JWT를 만들어 리턴
    // =====================================================
    public String create(String subject, long expireMin, Map<String, Object> claims) {
        Date expireDate = new Date(System.currentTimeMillis() + expireMin * 60 * 1000);

     // 변경 후 (신규 빌더 스타일)
        return Jwts.builder()
            .subject(subject)    // ← setSubject → subject
            .claims(claims)
            .expiration(expireDate)
            .signWith(key)
            .compact();
    }

    // =====================================================
    // 토큰 검증 및 클레임 반환
    // =====================================================
    public Map<String, Object> getClaims(String jwt) {
        try {
            JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

            Jws<Claims> jws = parser.parseSignedClaims(jwt);
            log.debug("claims : {}", jws);
            return jws.getPayload();
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.", e);
            throw e;
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error("유효하지 않은 토큰입니다.", e);
            throw e;
        }
    }
}
