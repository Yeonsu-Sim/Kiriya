package com.ssafy.kiriya.model.service.board;

import java.util.List;

import com.ssafy.kiriya.model.dto.Board;

public interface BoardService {

	// 게시판에 글추가
		public int add(Board board);
		
		// 게시판 원하는 제목만 찾기
		public List<Board> getBytitle(String title);
		
		// 게시판 전체 조회
		public List<Board> getAllBoards(int mno);
		
		// 게시판 글 수정
		public int set(Board board);
		
		// 게시판에 좋아요를 눌렀는지 조회
		public boolean getLike(int bno, int mno);
		
		// 게시판 좋아요 증가
		public int increaseLike(int bno, int mno);

		// 게시판 좋아요 취소
		int decreaseLike(int bno, int mno);
		
		// 게시판 글 삭제
		public int remove(int bno);
		
		// 게시글 번호로 검색(상세페이지 이동할때 씀)
		public Board getByBno(int bno);
		
		// 가져온 단어가 게시글에 있는지 확인
		public List<Board> boardFindWord(String text);

}
