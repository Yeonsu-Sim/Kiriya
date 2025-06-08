package com.ssafy.kiriya.model.dto.map;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {
    private List<Route> route;
    private int startId;
    private int endId;
}