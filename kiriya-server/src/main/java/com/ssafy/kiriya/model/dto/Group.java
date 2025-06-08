package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
	private int gno;
	private int mno;
	private String name;
	private String description;
	private String createdAt;
	private String updatedAt;
	
}
