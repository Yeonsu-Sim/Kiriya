package com.ssafy.kiriya.s3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.s3.service.S3ImageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class S3Controller implements ControllerHelper {
	private final S3ImageService s3ImageService;
	
	@PostMapping("/s3/upload")
	public ResponseEntity<?> s3Upload(@RequestPart(value = "image", required = false) MultipartFile image){
		System.out.println(image.toString());
		String profileImage = s3ImageService.upload(image);
		return handleSuccess(profileImage);
	}
}
