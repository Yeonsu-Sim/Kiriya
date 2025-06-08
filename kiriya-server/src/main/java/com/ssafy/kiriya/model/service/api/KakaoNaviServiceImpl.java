package com.ssafy.kiriya.model.service.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.kiriya.model.dto.api.DestinationsRequest;
import com.ssafy.kiriya.model.dto.api.DestinationsResponse;
import com.ssafy.kiriya.model.dto.api.DirectionsRequest;
import com.ssafy.kiriya.model.dto.api.DirectionsResponse;
import com.ssafy.kiriya.model.dto.api.DestinationsRequest.Destination;
import com.ssafy.kiriya.model.dto.api.DestinationsRequest.Origin;
import com.ssafy.kiriya.model.dto.map.Route;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoNaviServiceImpl implements KakaoNaviService{

	private final WebClient kakaoNaviClient;
	
	public int[] getDurations(int source, List<Route> routeList) {
		Origin origin = null;
    	List<Destination> destinations = new ArrayList<>();
    	for (int i=0; i<routeList.size(); i++) {
    		Route route = routeList.get(i);
    		if (i == source) {
    			origin = Origin.builder().x(route.getLongitude()).y(route.getLatitude()).build();
    		} else {
    			destinations.add(Destination.builder().x(route.getLongitude()).y(route.getLatitude()).key(i+"").build());
    		}
    	}
    	int[] durations = new int[routeList.size()];
    	DestinationsResponse response = kakaoNaviClient.post()
    			.uri("/v1/destinations/directions")
				.bodyValue(DestinationsRequest.builder()
						.origin(origin)
						.destinations(destinations)
						.build())
				.retrieve()
				.bodyToMono(DestinationsResponse.class)
				.block();
    	
		if (response.getRoutes() != null && !response.getRoutes().isEmpty()) {
			response.getRoutes().forEach(r -> {
				int key = Integer.parseInt(r.getKey());
				int duration = 987654321;
				if (r.getResult_code() == 0) duration = r.getSummary().getDuration();
				else System.out.println(r.getResult_msg());
				durations[key] = duration;
			});
		}
    	return durations;
	}
	
	public DirectionsResponse getDirections(DirectionsRequest request) {
    	DirectionsResponse response = kakaoNaviClient.get()
            .uri(uriBuilder -> {
                var builder = uriBuilder
                    .path("/v1/directions")
                    .queryParam("origin", request.getOrigin())
                    .queryParam("destination", request.getDestination());

                if (request.getPriority() != null)
                    builder.queryParam("priority", request.getPriority());
                if (request.getRoadevent() != null)
                    builder.queryParam("roadevent", request.getRoadevent());
                if (request.getRoadDetails() != null)
                    builder.queryParam("road_details", request.getRoadDetails());
                if (request.getCarHipass() != null)
                    builder.queryParam("car_hipass", request.getCarHipass());
                if (request.getSummary() != null)
                    builder.queryParam("summary", request.getSummary());

                return builder.build();
            })
            .retrieve()
            .bodyToMono(DirectionsResponse.class)
            .block();
        return response;
	}

}
