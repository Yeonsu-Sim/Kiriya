package com.ssafy.kiriya.model.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DirectionsResponse {
    private List<Route> routes;

    @Data
    public static class Route {
    	@JsonProperty("result_code")
        private int resultCode;
    	@JsonProperty("result_msg")
        private String resultMsg;
        private List<Section> sections;
    }

    @Data
    public static class Section {
    	private int distance; // 총 거리 (미터)
    	private int duration; // 총 소요 시간 (초)
    	private List<Location> guides;
    }
    
    @Data
    public static class Location {
        private String name;
        private double x;
        private double y;
    }

}
