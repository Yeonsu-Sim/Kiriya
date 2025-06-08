package com.ssafy.kiriya.model.service.api;

import java.util.List;

import com.ssafy.kiriya.model.dto.api.DirectionsRequest;
import com.ssafy.kiriya.model.dto.api.DirectionsResponse;
import com.ssafy.kiriya.model.dto.map.Route;

public interface KakaoNaviService {
	
	public int[] getDurations(int source, List<Route> routeList);
	
	public DirectionsResponse getDirections(DirectionsRequest request);
}
