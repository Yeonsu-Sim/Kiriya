package com.ssafy.kiriya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private int answerNo;
    private int questionNo;
    private int mno;
    private String content;
    private String author;
    private String authorProfileUrl;
    private String createdAt;
    private String updatedAt;
}
