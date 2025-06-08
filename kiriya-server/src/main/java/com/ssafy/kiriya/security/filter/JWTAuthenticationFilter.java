package com.ssafy.kiriya.security.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.service.member.MemberService;
import com.ssafy.kiriya.security.dto.CustomUserDetails;
import com.ssafy.kiriya.security.util.jwt.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JWTUtil jwtUtil;
    private final MemberService memberService;
    
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            MemberService memberService) {
		super.setAuthenticationManager(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.memberService = memberService;
	}
    
    // 로그인 성공 시 실행하는 메소드 (JWT 발급)
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
    		FilterChain chain,
            Authentication authentication) {
        // TODO: 05-1. 로그인 성공 시 Authentication의 정보를 활용해 JWT를 발급한다.
    	CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    	Member member = user.getMember();

    	String accessToken = jwtUtil.createAccessToken(member);
    	String refreshToken = jwtUtil.createRefreshToken(member);
    	
    	
    	Map<String, Object> result = Map.of("status", "SUCCESS", 
    			"accessToken", accessToken, "refreshToken", refreshToken,
    			"member", member);
    	handleResult(response, result, HttpStatus.OK);
    	
    }
    
    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) {
        handleResult(response, Map.of("status", "FAIL"), HttpStatus.UNAUTHORIZED);
    }

    // 결과 전송 helper 메소드
    private void handleResult(HttpServletResponse response, Map<String, ?> data, HttpStatus status) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(data);
            response.setStatus(status.value());
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            log.error("Error writing JSON response", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
