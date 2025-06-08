package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {
	private int no;
	private int gno;
	private int mno;
	private int role;
	private String joinedAt;
	private String memberName;
	private String memberEmail;
	private String profileUrl;
}
