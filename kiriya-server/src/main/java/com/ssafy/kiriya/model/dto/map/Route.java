package com.ssafy.kiriya.model.dto.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
	private int no;
    private int contentId;
    private int contentTypeId;
    private String title;
    private String addr1;
    private double longitude;
    private double latitude;
    private String firstImage1;
    private String firstImage2;
    private String startDate;
    private String endDate;
}
