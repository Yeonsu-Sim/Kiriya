package com.ssafy.kiriya.model.service.attraction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.kiriya.model.dao.AttractionDao;
import com.ssafy.kiriya.model.dto.Attraction;
import com.ssafy.kiriya.model.dto.Filter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {
	
	private final AttractionDao attractionDao;
	
	@Override
	public List<Attraction> getByFilter(Filter filter) {
		return attractionDao.getByFilter(filter);
	}
	
	@Override
	public List<Attraction> getByMno(int mno) {
		return attractionDao.getByMno(mno);
	}
	
	@Override
	public Attraction getByContentId(int contentId) {
		return attractionDao.getByContentId(contentId);
	}
	
	@Override
	public boolean getLike(int contentId, int mno) {
		Integer ano = attractionDao.getAnoByContentId(contentId);
		if (ano == null) throw new IllegalArgumentException("해당 관광지가 존재하지 않습니다.: "+contentId);
		return attractionDao.getLike(ano, mno) > 0;
	}
	
	@Override
	@Transactional
	public int increaseLike(int ano, int mno) {
		if (attractionDao.getLike(ano, mno) > 0) {
			throw new IllegalArgumentException("이미 버킷리스트에 담았습니다.");
		}
		int result = attractionDao.increaseLike(ano, mno);
		if (result != 1) {
			throw new IllegalArgumentException("버킷리스트에 담을 수 없습니다.");
		}
		return attractionDao.getAllLikes(ano);
	}
	
	@Override
	@Transactional
	public int decreaseLike(int ano, int mno) {
		int result = attractionDao.decreaseLike(ano, mno);
		return attractionDao.getAllLikes(ano);
	}

}
