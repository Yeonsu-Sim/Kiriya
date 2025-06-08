package com.ssafy.kiriya.security.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.kiriya.security.service.CustomUserDetailService;
import com.ssafy.kiriya.security.util.jwt.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTVerificationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final CustomUserDetailService userDetailsService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JWTVerificationFilter.doFilterInternal() called");
        try {
            // TODO: 04-2. JWT 토큰을 검증하고 인증 정보를 SecurityContextHolder에 저장한다.
            //  토큰 검증 및 사용자 정보 추출 - 토큰에 문제 없다면 사용자 정보는 신뢰할만하다.
        	String token = extractToken(request);
        	if(token == null) {
        		filterChain.doFilter(request, response);
        		return;
        	}
        	Map<String, Object> claims = jwtUtil.getClaims(token);
//        	String roleName = (String) claims.get("role");
//        	String authority = "ROLE_" + roleName;
            //  claim 정보를 이용한 사용자 정보 조회
        	UserDetails userDetails = userDetailsService.loadUserByUsername(claims.get("email").toString());
            //  UserNameAuthenticationToken 생성 및 SecurityContextHolder에 저장
        	var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        	SecurityContextHolder.getContext().setAuthentication(authentication);
            //  다음 filter로 요청을 전달
        	filterChain.doFilter(request, response);

            // END
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 04-3. 예외 발생 시 적절한 응답을 클라이언트에게 전달한다.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        // TODO: 04-1. Authentication 헤더가 'Bearer '로 시작하는지 확인하고, Bearer를 제거한 토큰을 반환한다.
    	String bearerToken = request.getHeader("Authorization");
    	if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
    		return bearerToken.substring(7);
    	} else {
    		return null;
    	}
        // END
    }
}
