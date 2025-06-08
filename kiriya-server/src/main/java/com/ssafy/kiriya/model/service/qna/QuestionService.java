package com.ssafy.kiriya.model.service.qna;

import java.util.List;

import com.ssafy.kiriya.model.dto.Question;

public interface QuestionService {
	void addQuestion(Question question);
	
	Question getQuestion(int questionNo);
	
	List<Question> getAllQuestions();
	
	void updateQuestion(Question question);
	
	void deleteQuestion(int questionNo);
}
