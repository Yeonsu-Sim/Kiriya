package com.ssafy.kiriya.model.service.role;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.RoleDao;
import com.ssafy.kiriya.model.dto.Role;

import lombok.RequiredArgsConstructor;

public interface RoleService {
	
	public List<Role> getAll();
	
}
