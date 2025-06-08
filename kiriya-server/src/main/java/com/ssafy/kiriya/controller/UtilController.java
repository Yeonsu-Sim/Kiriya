package com.ssafy.kiriya.controller;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Pair;
import com.ssafy.kiriya.model.service.util.UtilService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/util")
@RequiredArgsConstructor
public class UtilController implements ControllerHelper {
	
	private final UtilService utilService;
	
	@GetMapping("/sidos")
	private ResponseEntity<?> getSidos() {
		List<Pair> sidoList = utilService.getSidos();
    	return handleSuccess(Map.of("sidoList", sidoList));
	}
	
	@GetMapping("/gungus")
	private ResponseEntity<?> getGungusBySidoCode(@RequestParam Integer sidoCode) {
		List<Pair> gunguList = utilService.getGungusBySidoCode(sidoCode);
		return handleSuccess(Map.of("gunguList", gunguList));
	}
	
	@GetMapping("/content-types")
	private ResponseEntity<?> getContentTypes() {
		List<Pair> contentTypeList = utilService.getContentTypes();
		return handleSuccess(Map.of("contentTypeList", contentTypeList));
	}
}
