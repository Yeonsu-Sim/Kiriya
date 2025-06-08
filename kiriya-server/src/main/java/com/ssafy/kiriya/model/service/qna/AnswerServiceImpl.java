package com.ssafy.kiriya.model.service.qna;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.AnswerDao;
import com.ssafy.kiriya.model.dto.Answer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
	
	private final AnswerDao answerDao;
	@Override
	public void addAnswer(Answer answer) {
		answerDao.add(answer);
	}

	@Override
	public Answer getAnswer(int answerNo) {
		return answerDao.getByAnswerNo(answerNo);
	}

	@Override
	public List<Answer> getAnswersByQuestion(int questionNo) {
		return answerDao.getByQuestionNo(questionNo);
	}

	@Override
	public void updateAnswer(Answer answer) {
		answerDao.update(answer);
	}

	@Override
	public void deleteAnswer(int answerNo) {
		answerDao.delete(answerNo);
	}

}
