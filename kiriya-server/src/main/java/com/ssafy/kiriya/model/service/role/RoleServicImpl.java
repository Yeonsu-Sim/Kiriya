package com.ssafy.kiriya.model.service.role;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.RoleDao;
import com.ssafy.kiriya.model.dto.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServicImpl implements RoleService {
	private final RoleDao roleDao;
	
	public List<Role> getAll() {
		try {
			return roleDao.getAll();			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new DataRetrievalFailureException("역할 목록 조회 실패", e);
		}
	}
}
