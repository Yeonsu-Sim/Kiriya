package com.ssafy.kiriya.model.dto;

import lombok.Data;

@Data
public class Comment {
    private int cno;
    private int bno;
    private int mno;
    private String author;    // 작성자 이름 (join)
    private String authorProfileUrl; // 작성자 프로필 (선택)
    private String content;
    private String createdAt;
    private String updatedAt;
}
