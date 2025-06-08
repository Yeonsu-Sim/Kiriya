package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Question;

@Mapper
public interface QuestionDao {
    /** 질문 등록 후, 생성된 questionNo를 question 객체에 세팅하여 반환 */
    void add(Question question);

    /** 단일 질문 조회 */
    Question getByQuestionNo(int questionNo);

    /** 전체 질문 목록 조회 (최신 순) */
    List<Question> getAllQuestions();

    /** 질문 수정 */
    void update(Question question);

    /** 질문 삭제 */
    void delete(int questionNo);
}
