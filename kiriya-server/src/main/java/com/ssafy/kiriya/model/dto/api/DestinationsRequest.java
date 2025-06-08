package com.ssafy.kiriya.model.dto.api;

import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class DestinationsRequest {
	private Origin origin;
    private List<Destination> destinations;
    @Default
    private int radius = 10000;

    @Data
    @Builder
    public static class Origin {
        private double x;
        private double y;
    }

    @Data
    @Builder
    public static class Destination {
        private double x;
        private double y;
        private String key;
    }

}
