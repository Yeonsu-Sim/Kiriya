package com.ssafy.kiriya.model.service.board;

import java.util.List;

import com.ssafy.kiriya.model.dto.Comment;

public interface CommentService {

	void addComment(Comment comment);
	
	List<Comment> getCommentsByBno(int bno);
	
	void updateComment(Comment comment);
	
	void deleteComment(int cno);
}
