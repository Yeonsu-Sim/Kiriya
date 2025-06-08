package com.ssafy.kiriya.model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
	private int tno;
    private int gno;
    private int mno;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String createdAt;
    private String updatedAt;
    private String thumbnailUrl;
    
    public String getStartDateOnly() {
        try {
            LocalDateTime dt = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return dt.toLocalDate().toString();
        } catch (Exception e) {
            return startDate; // 파싱 실패 시 원본 반환
        }
    }
    
    public String getEndDateOnly() {
    	try {
    		LocalDateTime dt = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    		return dt.toLocalDate().toString();
    	} catch (Exception e) {
    		return endDate; // 파싱 실패 시 원본 반환
    	}
    }
}