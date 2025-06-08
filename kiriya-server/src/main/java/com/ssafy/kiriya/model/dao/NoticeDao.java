package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Notice;

@Mapper
public interface NoticeDao {
	
	// 공지사항에 글추가
	public int add(Notice notice);
	
	// 공지사항 원하는 제목만 찾기
	public List<Notice> getBytitle(String title);
	
	// 공지사항 전체 조회
	public List<Notice> getAllNotices();
	
	// 공지사항 글 수정
	public int set(Notice notice);
	
	// 공지사항을 nno로 가져오기
	public Notice getByNno(int nno);
	
	// 공지사항 글 삭제
	public int remove(int nno);

	public int removeAll(int mno);

}
