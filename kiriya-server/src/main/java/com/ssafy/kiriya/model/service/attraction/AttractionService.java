package com.ssafy.kiriya.model.service.attraction;

import java.util.List;

import com.ssafy.kiriya.model.dto.Attraction;
import com.ssafy.kiriya.model.dto.Filter;

public interface AttractionService {

	public List<Attraction> getByFilter(Filter filter);
	
	public Attraction getByContentId(int contentId);
	
	public boolean getLike(int contentId, int mno);
	
	public int increaseLike(int ano, int mno);

	public int decreaseLike(int ano, int mno);

	public List<Attraction> getByMno(int mno);
}
