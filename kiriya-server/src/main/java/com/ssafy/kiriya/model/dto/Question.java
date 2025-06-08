package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private int questionNo;
    private int mno;
    private String title;
    private String content;
    private String author;
    private String authorProfileUrl;
    private String createdAt;
    private String updatedAt;
}