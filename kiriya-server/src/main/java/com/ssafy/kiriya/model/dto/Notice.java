package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	int nno;
	int mno;
	String title;
	String content;
	String author;
	String createdAt;
	String updatedAt;
}
