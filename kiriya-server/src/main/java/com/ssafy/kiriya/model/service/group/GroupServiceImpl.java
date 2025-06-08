package com.ssafy.kiriya.model.service.group;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.GroupDao;
import com.ssafy.kiriya.model.dao.MemberDao;
import com.ssafy.kiriya.model.dto.Group;
import com.ssafy.kiriya.model.dto.GroupMember;
import com.ssafy.kiriya.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupDao groupDao;
	private final MemberDao memberDao;

	@Override
	public int createGroup(Group group, List<String> emails) {
		// 이메일 유효성 검사
		List<String> invalids = emails.stream()
				.filter(email->memberDao.getByEmail(email)==null)
				.collect(Collectors.toList());
		if(!invalids.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 회원 이메일: " + invalids);
		}
		
		// 그룹 테이블에 insert
		groupDao.createGroup(group);
		groupDao.addGroupMember(group.getGno(), group.getMno(), 2); // 방장 그룹에 추가

		// 나머지 멤버 추가
		for (String email : emails) {
			Member m = memberDao.getByEmail(email);
			groupDao.addGroupMember(group.getGno(), m.getMno(), 2);

		}
		
		return group.getGno();
	}

	@Override
	public List<Group> getGroupsByMember(int mno) {
		return groupDao.selectGroupsByMember(mno);
	}

	@Override
	public List<GroupMember> getMembersByGroup(int gno) {
		return groupDao.selectMembersByGroup(gno);
	}

	@Override
	public void addMember(int gno, String email) {
		Member m = memberDao.getByEmail(email);
		if (m != null) {
			groupDao.addGroupMember(gno, m.getMno(), 2);
		}
	}
	
	@Override
	public void deleteMember(int gno, String email) {
		Member m = memberDao.getByEmail(email);
		if(m==null) {
			throw new IllegalArgumentException("존재하지 않는 이메일입니다: "+email);
		}
		int removed = groupDao.deleteGroupMember(gno, m.getMno());
		if(removed==0) {
			throw new IllegalArgumentException("그룹원 삭제에 실패했습니다.");
		}
	}

	@Override
	public Group getGroupByGno(int gno) {
		return groupDao.selectGroupByGno(gno);
	}

	@Override
	public void updateGroup(Group group, List<String> emails) {
		// 이메일 유효성 검증 
		List<String> invalids = emails.stream()
            .filter(email -> memberDao.getByEmail(email) == null)
            .collect(Collectors.toList());
        if (!invalids.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 회원 이메일: " + invalids);
        }

        // 그룹 정보 업데이트
        int updated = groupDao.updateGroup(group);
        if (updated == 0) {
            throw new IllegalArgumentException("존재하지 않는 그룹입니다: gno=" + group.getGno());
        }
        
        groupDao.deleteGroupMembers(group.getGno());
        groupDao.addGroupMember(group.getGno(), group.getMno(), 2);
        for(String email : emails) {
        	Member m = memberDao.getByEmail(email);
        	groupDao.addGroupMember(group.getGno(), m.getMno(), 2);
        }

	}

	@Override
	public void deleteGroup(int gno) {
		groupDao.deleteGroupMembers(gno);
		groupDao.deleteGroup(gno);
		
	}

}
