package com.ssafy.kiriya.model.dto.map;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteResponse {
	private List<Route> route;
}
