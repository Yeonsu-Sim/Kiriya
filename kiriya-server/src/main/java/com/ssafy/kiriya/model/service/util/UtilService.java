package com.ssafy.kiriya.model.service.util;

import java.util.List;

import com.ssafy.kiriya.model.dto.Pair;
public interface UtilService {
	
	public String getSidoName(int sidoCode);
	
	public String getGunguName(int gunguCode);
	
	public String getContentTypeName(int contentTypeId);
	
	public List<Pair> getSidos();
	
	public List<Pair> getGungusBySidoCode(int sidoCode);

	public List<Pair> getContentTypes();
}
