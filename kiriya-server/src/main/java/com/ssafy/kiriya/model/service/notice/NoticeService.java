package com.ssafy.kiriya.model.service.notice;

import java.util.List;

import com.ssafy.kiriya.model.dto.Notice;

public interface NoticeService {

	// 공지사항에 글추가
	public int add(Notice notice);

	// 공지사항 원하는 제목만 찾기
	public List<Notice> getBytitle(String title);

	// 공지사항 전체 조회
	public List<Notice> getAllNotices();

	// 공지사항 글 수정
	public int set(Notice notice);

	// 공지사항 글 삭제
	public int remove(int nno);

	// 공지사항 번호로 검색
	public Notice getNoticeNno(int nno);

	// 가져온 단어가 공지사항에 있는지 확인
	public List<Notice> noticeFindWord(String text);

}
