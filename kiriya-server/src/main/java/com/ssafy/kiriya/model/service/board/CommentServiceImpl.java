package com.ssafy.kiriya.model.service.board;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.CommentDao;
import com.ssafy.kiriya.model.dto.Comment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentDao commentDao;
	
	@Override
	public void addComment(Comment comment) {
		commentDao.add(comment);
	}

	@Override
	public List<Comment> getCommentsByBno(int bno) {
		return commentDao.getCommentsByBno(bno);
	}

	@Override
	public void updateComment(Comment comment) {
		commentDao.update(comment);
	}

	@Override
	public void deleteComment(int cno) {
		commentDao.delete(cno);
	}

}
