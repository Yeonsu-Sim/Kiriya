package com.ssafy.kiriya.model.service.group;

import java.util.List;

import com.ssafy.kiriya.model.dto.Group;
import com.ssafy.kiriya.model.dto.GroupMember;

public interface GroupService {

	int createGroup(Group group, List<String> emails);
    List<Group> getGroupsByMember(int mno);
    List<GroupMember> getMembersByGroup(int gno);
    void addMember(int gno, String email);
	Group getGroupByGno(int gno);
	void updateGroup(Group group, List<String> emails);
	void deleteMember(int gno, String email);
	void deleteGroup(int gno);
    
}
