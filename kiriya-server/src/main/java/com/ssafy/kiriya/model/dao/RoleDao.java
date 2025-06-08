package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Role;

@Mapper
public interface RoleDao {
	public List<Role> getAll();
}
