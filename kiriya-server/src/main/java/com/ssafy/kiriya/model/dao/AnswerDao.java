package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Answer;

@Mapper
public interface AnswerDao {
    /** 답변 등록 후, 생성된 answerNo를 answer 객체에 세팅하여 반환 */
    void add(Answer answer);

    /** 단일 답변 조회 */
    Answer getByAnswerNo(int answerNo);

    /** 특정 질문에 달린 모든 답변 조회 */
    List<Answer> getByQuestionNo(int questionNo);

    /** 답변 수정 */
    void update(Answer answer);

    /** 답변 삭제 */
    void delete(int answerNo);
}
