package com.ssafy.kiriya.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.MemberDao;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.security.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	
	private final MemberDao memberDao;

	@Override
	public UserDetails loadUserByUsername(String email) {
	    Member member = memberDao.getByEmail(email);
	    if (member == null) {
	        throw new UsernameNotFoundException(email);
	    }
	    return new CustomUserDetails(member);
	}
}

