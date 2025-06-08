package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Attraction;
import com.ssafy.kiriya.model.dto.Filter;

@Mapper
public interface AttractionDao {
	
	public List<Attraction> getByFilter(Filter filter);
	
	public Attraction getByContentId(int contentId);
	
	public Integer getAnoByContentId(int contentId);
	
	public int getLike(int ano, int mno);
	
	public int increaseLike(int ano, int mno);
	
	public int decreaseLike(int ano, int mno);
	
	public int getAllLikes(int ano);
	
	public List<Attraction> getByMno(int mno);

}
