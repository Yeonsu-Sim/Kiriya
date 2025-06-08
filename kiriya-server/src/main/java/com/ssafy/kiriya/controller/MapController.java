package com.ssafy.kiriya.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.api.DirectionsRequest;
import com.ssafy.kiriya.model.dto.map.KeysResponse;
import com.ssafy.kiriya.model.dto.map.Route;
import com.ssafy.kiriya.model.dto.map.RouteRequest;
import com.ssafy.kiriya.model.service.api.KakaoNaviService;
import com.ssafy.kiriya.model.service.map.MapService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController implements ControllerHelper {
    private final MapService mapService;
    private final KakaoNaviService kakaoNaviService;
    
    /* API Keys */
    @Value("${api.key_vworld}")
    private String keyVworld;
    @Value("${api.key_sgis_service_id}")
	private String keySgisServiceId;  // 서비스 id
	@Value("${api.key_sgis_security}")
	private String keySgisSecurity;  // 보안 key
	@Value("${api.key_data}")
	private String keyData; // data.go.kr 인증키
    
	@GetMapping("/keys")
	private ResponseEntity<?> getKeys() {
		if (keyVworld == null) {
			log.error("keyVworld 키 누락");
			return handleFail(new RuntimeException("keyVworld 키가 누락되었습니다."));
		} else if (keySgisServiceId == null) {
			log.error("keySgisServiceId 키 누락");
			return handleFail(new RuntimeException("keySgisServiceId 키가 누락되었습니다."));
		} else if (keySgisSecurity == null) {
			log.error("keySgisSecurity 키 누락");
			return handleFail(new RuntimeException("keySgisSecurity 키가 누락되었습니다."));
		} else if (keyData == null) {
			log.error("keyData 키 누락");
			return handleFail(new RuntimeException("keyData 키가 누락되었습니다."));
		}
		KeysResponse response = KeysResponse.builder()
									.keyVworld(keyVworld)
									.keySgisServiceId(keySgisServiceId)
									.keySgisSecurity(keySgisSecurity)
									.keyData(keyData)
									.build();
		return handleSuccess(response);
	}
	
    @PostMapping("/route")
    @ResponseBody
    public ResponseEntity<?> getRoute(@RequestBody RouteRequest request) {
        	log.debug("경로 요청 수신: " + request);
        	
            List<Route> routeList = request.getRoute();
            int startId = request.getStartId();
            int endId = request.getEndId();
            
            int[][] adjMat = new int[routeList.size()][];
            
            for (int i=0; i<adjMat.length; i++) {
            	adjMat[i] = kakaoNaviService.getDurations(i, routeList);
            }
            
            for (int[] row: adjMat)
        		System.out.println("adjMat: "+Arrays.toString(row));

            List<Route> route = mapService.getRoute(routeList, adjMat, startId, endId);
            return handleSuccess(Map.of("route", route));
    }
    
    @PostMapping("/directions")
    @ResponseBody
    public ResponseEntity<?> getDirections(@RequestBody DirectionsRequest request) {
    		log.debug("길찾기 요청 수신: " + request);
    		return handleSuccess(kakaoNaviService.getDirections(request).getRoutes().get(0));
    }
}