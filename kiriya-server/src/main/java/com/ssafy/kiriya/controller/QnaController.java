package com.ssafy.kiriya.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Answer;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.dto.Question;
import com.ssafy.kiriya.model.service.qna.AnswerService;
import com.ssafy.kiriya.model.service.qna.QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/qnas")
@RequiredArgsConstructor
public class QnaController implements ControllerHelper {
	private final QuestionService questionService;
	private final AnswerService answerService;
	
	// --- 질문 ---
	
	@GetMapping("/questions")
	public ResponseEntity<?> getAllQuestions() {
		List<Question> questions = questionService.getAllQuestions();
		return handleSuccess(questions);
	}
	
	@GetMapping("/questions/{questionNo}")
	public ResponseEntity<?> getQuestion(@PathVariable int questionNo) {
		Question q = questionService.getQuestion(questionNo);
		if(q==null) {
			throw new IllegalArgumentException("존재하지 않는 질문입니다.");
		}
		return handleSuccess(q);
	}
	
	@PostMapping("/questions")
	public ResponseEntity<?> createQuestion(@RequestBody Question question, @AuthenticationPrincipal Member member){
		if(member != null) {
			question.setMno(member.getMno());
		}
		questionService.addQuestion(question);
		Question created = questionService.getQuestion(question.getQuestionNo());
		return handleSuccess(created, HttpStatus.CREATED);

	}
	
	@PutMapping("/questions/{questionNo}")
	public ResponseEntity<?> updateQuestion(@PathVariable int questionNo, @RequestBody Question q) {
		q.setQuestionNo(questionNo);
		questionService.updateQuestion(q);
		Question updated = questionService.getQuestion(questionNo);
		return handleSuccess(updated);
	}
	
	@DeleteMapping("/questions/{questionNo}")
	public ResponseEntity<?> deleteQuestion(@PathVariable int questionNo) {
		questionService.deleteQuestion(questionNo);
		return handleSuccess("삭제되었습니다.");
	}
	
	
	
	
	// --- 답변 ---
	
	@GetMapping("/questions/{questionNo}/answers")
	public ResponseEntity<?> getAnswers(@PathVariable int questionNo) {
		List<Answer> answers = answerService.getAnswersByQuestion(questionNo);
		return handleSuccess(answers);
	}
	
	@GetMapping("/answers/{answerNo}")
	public ResponseEntity<?> getAnswer(@PathVariable int answerNo) {
		Answer ans = answerService.getAnswer(answerNo);
		if(ans==null) {
			throw new IllegalArgumentException("존재하지 않는 답변입니다.");
		}
		return handleSuccess(ans);
	}
	
	@PostMapping("/questions/{questionNo}/answers")
	public ResponseEntity<?> createAnswer(@PathVariable int questionNo, @RequestBody Answer a, @AuthenticationPrincipal Member member) {
		if (member != null) {
	        if (member.getRole().getNo()!=1) {
	        	throw new IllegalArgumentException("답변 작성 권한이 없습니다.");	        	
	        }	        
	        a.setMno(member.getMno());
	    }
		System.out.println("answer: "+a);
		a.setQuestionNo(questionNo);
		answerService.addAnswer(a);
		Answer created = answerService.getAnswer(a.getAnswerNo());
		return handleSuccess(created, HttpStatus.CREATED);
	}
	
	@PutMapping("/answers/{answerNo}")
    public ResponseEntity<?> updateAnswer(@PathVariable int answerNo, @RequestBody Answer a, @AuthenticationPrincipal Member member) {
		if (member != null && member.getRole().getNo()!=1) {
	        throw new IllegalArgumentException("답변 수정 권한이 없습니다.");
	    }
		a.setAnswerNo(answerNo);
        answerService.updateAnswer(a);
        Answer updated = answerService.getAnswer(answerNo);
        return handleSuccess(updated);
    }

    @DeleteMapping("/answers/{answerNo}")
    public ResponseEntity<?> deleteAnswer(@PathVariable int answerNo, @AuthenticationPrincipal Member member) {
    	if (member != null && member.getRole().getNo()!=1) {
	        throw new IllegalArgumentException("답변 삭제 권한이 없습니다.");
	    }
    	answerService.deleteAnswer(answerNo);
        return handleSuccess("삭제되었습니다.");
    }
}
