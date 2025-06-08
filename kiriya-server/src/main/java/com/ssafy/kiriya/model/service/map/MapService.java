package com.ssafy.kiriya.model.service.map;

import java.util.List;

import com.ssafy.kiriya.model.dto.map.Route;

public interface MapService {
	
	public List<Route> getRoute(List<Route> route, int[][] adjMat, int startId, int endId);
	
}
