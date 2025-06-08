package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attraction {
	int no;
	int contentId;
	String title;
	int contentTypeId;
	int areaCode;
	int siGunGuCode;
	String firstImage1;
	String firstImage2;
	int mapLevel;
	double latitude;
	double longitude;
	String tel;
	String addr1;
	String addr2;
	String homepage;
	String overview;
	int likes;
	boolean liked;
}
