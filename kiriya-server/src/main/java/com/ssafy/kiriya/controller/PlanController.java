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
import com.ssafy.kiriya.model.dto.map.Route;
import com.ssafy.kiriya.model.service.map.RouteService;
import com.ssafy.kiriya.model.service.plan.PlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController implements ControllerHelper {
	private final PlanService planService;
	private final RouteService routeService;
	
	@PostMapping
	private ResponseEntity<?> add(@RequestBody Plan plan) {
		log.debug("plan: {}",plan);
		planService.add(plan);
		System.out.println("pno: "+plan.getPno());
		routeService.addAll(plan.getPno(), plan.getRoute());
		return handleSuccess(Map.of("message", "등록되었습니다."), HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{pno}")
	private ResponseEntity<?> get(@PathVariable Integer pno) {
		Plan plan = planService.getByPno(pno);
		List<Route> route = routeService.getByPno(pno);
		plan.setRoute(route);
		log.debug("plan: {}", plan);
		return handleSuccess(Map.of("plan", plan));
	}
	
	@PutMapping("/{pno}")
	private ResponseEntity<?> update(@PathVariable Integer pno, @RequestBody Plan plan) {
		List<Route> route = plan.getRoute();
		log.debug("update plan: "+plan);
		if (route != null) {
			routeService.removeAll(pno);
			routeService.addAll(pno, route);
		} else {			
			planService.set(plan);
		}
		return handleSuccess(Map.of("message", "수정되었습니다."), HttpStatus.NO_CONTENT);
	}	
	
	@DeleteMapping("/{pno}")
	private ResponseEntity<?> delete(@PathVariable Integer pno) {
		planService.remove(pno);
		return handleSuccess(Map.of("message", "삭제되었습니다."), HttpStatus.NO_CONTENT);
	}
	
}