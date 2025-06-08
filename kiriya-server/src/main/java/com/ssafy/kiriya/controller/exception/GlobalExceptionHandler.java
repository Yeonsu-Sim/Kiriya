package com.ssafy.kiriya.controller.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ssafy.kiriya.controller.helper.ControllerHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler implements ControllerHelper {
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<?> handleDataAccessException(DataAccessException e) {
		log.error("DB 접근 오류: {}", e.getMessage());
		return handleFail(new RuntimeException("DB 접근 오류: " + e.getMessage()));
	}

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> handleWebClientResponseException(WebClientResponseException e) {
        log.error("API 오류 응답: statusCode={}, body={}", e.getStatusCode(), e.getResponseBodyAsString(), e);
        return handleFail(new RuntimeException("API 오류 응답: " + e.getResponseBodyAsString()), e.getStatusCode());
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<?> handleWebClientRequestException(WebClientRequestException e) {
        log.error("API 요청 실패: {}", e.getMessage(), e);
        return handleFail(new RuntimeException("API 요청 실패: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 입력: {}", e.getMessage(), e);
        return handleFail(new RuntimeException("잘못된 입력: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception e) {
        log.error("예기치 못한 오류 발생: {}", e.getMessage(), e);
        return handleFail(new RuntimeException("예기치 못한 오류: " + e.getMessage()));
    }
}