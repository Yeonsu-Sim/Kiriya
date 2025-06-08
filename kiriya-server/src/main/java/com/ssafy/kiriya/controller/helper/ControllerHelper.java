package com.ssafy.kiriya.controller.helper;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public interface ControllerHelper {
	default ResponseEntity<?> handleSuccess(Object data) {
        return handleSuccess(data, HttpStatus.OK);
    }

    default ResponseEntity<?> handleFail(Throwable e) {
        return handleFail(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    default ResponseEntity<?> handleSuccess(Object data, HttpStatusCode status) {
        Map<String, Object> map = Map.of("status", "SUCCESS", "data", data);
        return ResponseEntity.status(status).body(map);
    }

    default ResponseEntity<?> handleFail(Throwable e, HttpStatusCode status) {
        e.printStackTrace();
        Map<String, Object> map = Map.of("status", "FAIL", "error", e.getMessage());
        return ResponseEntity.status(status).body(map);
    }
}
