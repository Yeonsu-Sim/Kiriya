package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Pair;
@Mapper
public interface UtilDao {
	public String getSidoName(int sidoCode);

	public String getGunguGugunName(int gunguCode);

	public String getContentTypeName(int contentTypeId);
	
	public List<Pair> getSidos();
	
	public List<Pair> getGungusBySidoCode(int sidoCode);

	public List<Pair> getContentTypes();
}
