package com.ssafy.kiriya.model.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DirectionsRequest {
	private String origin; // 예: "127.111202,37.394912"
    private String destination; // 예: "127.110563,37.394196"
    private String priority; // "RECOMMEND", "TIME", "DISTANCE"
    private Integer roadevent;
    @JsonProperty("road_details")
    private Boolean roadDetails;
    @JsonProperty("car_hipass")
    private Boolean carHipass;
    private Boolean summary;
}
