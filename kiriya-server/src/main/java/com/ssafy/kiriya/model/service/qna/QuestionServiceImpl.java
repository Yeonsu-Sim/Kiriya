package com.ssafy.kiriya.model.service.qna;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.QuestionDao;
import com.ssafy.kiriya.model.dto.Question;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
	
	private final QuestionDao questionDao;

	@Override
	public void addQuestion(Question question) {
		questionDao.add(question);
	}

	@Override
	public Question getQuestion(int questionNo) {
		return questionDao.getByQuestionNo(questionNo);
	}

	@Override
	public List<Question> getAllQuestions() {
		return questionDao.getAllQuestions();
	}

	@Override
	public void updateQuestion(Question question) {
		questionDao.update(question);
		
	}

	@Override
	public void deleteQuestion(int questionNo) {
		questionDao.delete(questionNo);
		
	}

}
