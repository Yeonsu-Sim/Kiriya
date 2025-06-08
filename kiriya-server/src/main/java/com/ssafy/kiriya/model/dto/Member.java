package com.ssafy.kiriya.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Member {
	int mno;
	String name;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String password;
	
	String email;
	Role role;
	String profileUrl;
	
	public Member() {}
	
	public Member(int mno, String name, String password, String email, Role role, String profileUrl) {
		this.mno = mno;
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.profileUrl = profileUrl;
	}
	
	public Member(String name, String password, String email, Role role, String profileUrl) {
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.profileUrl = profileUrl;
	}

}
