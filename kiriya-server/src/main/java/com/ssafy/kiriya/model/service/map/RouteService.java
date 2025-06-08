package com.ssafy.kiriya.model.service.map;

import java.util.List;

import com.ssafy.kiriya.model.dto.map.Route;

public interface RouteService {
	
	void addAll(int pno, List<Route> route);
	
	List<Route> getByPno(int pno);
	
	void removeAll(int pno);
}
