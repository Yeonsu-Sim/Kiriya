package com.ssafy.kiriya.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Plan;
import com.ssafy.kiriya.model.dto.Trip;
import com.ssafy.kiriya.model.service.map.RouteService;
import com.ssafy.kiriya.model.service.plan.PlanService;
import com.ssafy.kiriya.model.service.trip.TripService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController implements ControllerHelper {
	private final TripService tripService;
	private final PlanService planService;
	private final RouteService routeService;
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Trip trip) {
		log.debug("trip: {}", trip);
		tripService.add(trip);
		return handleSuccess("등록되었습니다.", HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<?> getAll() {
		List<Trip> tripList = tripService.getAll();
		return handleSuccess(Map.of("tripList", tripList));
	}
	
	@GetMapping("/{tno}")
	public ResponseEntity<?> get(@PathVariable Integer tno) {
		log.debug("tno: {}", tno);
		Trip trip = tripService.get(tno);
		return handleSuccess(Map.of("trip", trip));
	}
	
	@GetMapping("/{tno}/plans")
	public ResponseEntity<?> getPlansByTno(@PathVariable Integer tno) {
		log.debug("tno: {}", tno);
		List<Plan> planList = planService.getByTno(tno);
		for (Plan plan : planList) {
	        plan.setRoute(routeService.getByPno(plan.getPno()));
	    }
		log.debug("plan: {}", planList);
		return handleSuccess(Map.of("planList", planList));
	}
	
	@PutMapping("/{tno}")
	public ResponseEntity<?> update(@PathVariable Integer tno, @RequestBody Trip trip) {
		tripService.update(trip);
		return handleSuccess(Map.of("message", "수정되었습니다."), HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/{tno}")
	public ResponseEntity<?> delete(@PathVariable Integer tno) {
		tripService.delete(tno);
		return handleSuccess(Map.of("message", "삭제되었습니다."), HttpStatus.NO_CONTENT);
	}
}
