package com.ssafy.kiriya.model.dto.api;

import java.util.List;

import lombok.Data;

@Data
public class DestinationsResponse {
    private List<Route> routes;

    @Data
    public static class Route {
    	private int result_code;
    	private String result_msg;
    	private String key;
        private Summary summary;
    }

    @Data
    public static class Summary {
        private int distance;
        private int duration;
    }
}
