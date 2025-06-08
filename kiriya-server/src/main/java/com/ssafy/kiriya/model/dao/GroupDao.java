package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Group;
import com.ssafy.kiriya.model.dto.GroupMember;

@Mapper
public interface GroupDao {

	// 그룹 추가
	public int createGroup(Group group);
	
	// 그룹구성원 추가
	public int addGroupMember(int gno, int mno, int role);
	
	public List<Group> selectGroupsByMember(int mno);
	
	public List<GroupMember> selectMembersByGroup(int gno);
	
	public Group selectGroupByGno(int gno);
	
	public int updateGroup(Group group);
	
	public int deleteGroupMember(int gno, int mno);
	
	public int deleteGroupMembers(int gno);
	
	public int deleteGroup(int gno);
	
}
