package com.ssafy.kiriya.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	int bno;
	int mno;
	String title;
	String content;
	String author;
	String profileUrl;
	String createdAt;
	String updatedAt;
	int likes;
	boolean liked;
	int commentCount;
	
	private List<String> imageUrls;
	private List<String> tags;
}
