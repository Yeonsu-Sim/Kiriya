package com.ssafy.kiriya.model.service.qna;

import java.util.List;

import com.ssafy.kiriya.model.dto.Answer;

public interface AnswerService {

	void addAnswer(Answer answer);
	
	Answer getAnswer(int answerNo);
	
	List<Answer> getAnswersByQuestion(int questionNo);
	
	void updateAnswer(Answer answer);
	
	void deleteAnswer(int answerNo);
}
