package com.ssafy.kiriya.model.service.util;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.UtilDao;
import com.ssafy.kiriya.model.dto.Pair;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {
	private final UtilDao utilDao;
	
	@Override
	public String getSidoName(int sidoCode) {
		return null;
	}

	@Override
	public String getGunguName(int gunguCode) {
		return null;
	}

	@Override
	public String getContentTypeName(int contentTypeId) {
		return null;
	}
	@Override
	public List<Pair> getSidos() {
		return utilDao.getSidos();
	}
	@Override
	public List<Pair> getGungusBySidoCode(int sidoCode) {
		return utilDao.getGungusBySidoCode(sidoCode);
	}
	@Override
	public List<Pair> getContentTypes() {
		return utilDao.getContentTypes();
	}

}
