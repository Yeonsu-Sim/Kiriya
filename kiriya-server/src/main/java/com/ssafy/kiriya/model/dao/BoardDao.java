package com.ssafy.kiriya.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.kiriya.model.dto.Board;
import com.ssafy.kiriya.model.dto.Group;
import com.ssafy.kiriya.model.dto.GroupMember;

@Mapper
public interface BoardDao {
	
	// 게시판에 글추가
	public int add(Board board);
	
	// 게시판 원하는 제목만 찾기
	public List<Board> getBytitle(String title);
	
	// 게시판 전체 조회
	public List<Board> getAllBoards(int mno);
	
	// 게시판에 좋아요를 눌렀는지 조회
	public int getLike(int bno, int mno);
	
	// 게시판 글 수정
	public int set(Board board);

	// 게시판 좋아요 증가
	public int increaseLike(int bno, int mno);
	
	// 게시판 좋아요 취소
	public int decreaseLike(int bno, int mno);
	
	// 게시판 좋아요 개수 조회
	public int getAllLikes(int bno);
	
	// 게시글을 bno로 가져오기
	public Board getByBno(int bno);
	
	// 게시판 글 삭제
	public int remove(int bno);
	
	// 게시글 좋아요 null 설정
	public int nullLike(int bno);

	public int removeAll(int mno);
	
	// 게시글에 이미지 한 장 추가
	public int addImage(int bno, String url, int seq);
	
	// 게시글의 이미지 찾기
	public List<String> getImagesByBno(int bno);
	
	// 게시글의 모든 이미지 삭제 
	public int removeImagesByBno(int bno);
	
	// 새 태그 등록
	public int addTag(String name);
	
	// 게시글-태그 연결 추가
	public int addBoardTag(int bno, int tagId);
	
	// 게시글의 태그 연결 삭제
	public int removeBoardTagsByBno(int bno);
	
	// 태그 이름으로 id 조회 
	public Integer findTagByName(String name);
	
	// 게시글-태그들 가져오기 
	public List<String> getTagsByBno(int bno);

}
