package com.ssafy.kiriya.model.dto;

import lombok.Data;

@Data
public class Role {
	int no;
	String roleName;
	
	public Role() {}

	public Role(int no, String roleName) {
		super();
		this.no = no;
		this.roleName = roleName;
	}
}