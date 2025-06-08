package com.ssafy.kiriya.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Member;

@Mapper
public interface MemberDao {
	
	public int add(Member member);
	
	public Member get(int mno);
	
	public int set(Member member);
	
	public int updateAvatar(int mno, String profileUrl);
	
	public int remove(int mno);

	public Member getByEmail(String email);
	
	public Member selectByEmail(String email);

}
