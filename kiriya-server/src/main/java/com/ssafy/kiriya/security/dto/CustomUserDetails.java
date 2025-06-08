package com.ssafy.kiriya.security.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.dto.Role;

/**
 * 스프링 시큐리티용 UserDetails 구현체
 * 내부에 Member 도메인을 감싸고, 권한 정보를 제공
 */
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    /** 컨트롤러나 필터에서 원본 Member를 꺼낼 때 사용 */
    public Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Member.getRole().getRoleName() 예: "USER" 또는 "ADMIN"
    	Role role = member.getRole();
        String roleName = (role != null && role.getRoleName() != null)
            ? role.getRoleName()
            : "USER";  // 디폴트로 USER 역할 부여
        // 스프링 시큐리티는 "ROLE_USER" 형식을 기대하므로 접두어 추가
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    // 필요하다면 Member 필드로 체크할 수 있고, 없으면 모두 true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
