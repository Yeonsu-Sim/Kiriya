package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Comment;

@Mapper
public interface CommentDao {
	void add(Comment comment);

	List<Comment> getCommentsByBno(int bno);
	
	void update(Comment comment);
	
	void delete(int cno);
}
