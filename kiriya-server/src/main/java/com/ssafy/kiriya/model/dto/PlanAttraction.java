package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanAttraction {
    private int no;
    private int pno;
    private int ano;
    private int sequenceOrder;
    private String startDate;
    private String endDate;
}