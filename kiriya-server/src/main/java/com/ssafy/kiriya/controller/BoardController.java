package com.ssafy.kiriya.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Board;
import com.ssafy.kiriya.model.dto.Comment;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.service.board.BoardService;
import com.ssafy.kiriya.model.service.board.CommentService;
import com.ssafy.kiriya.security.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController implements ControllerHelper {
    private final BoardService boardService;
    private final CommentService commentService;
	
    @PostMapping
	public ResponseEntity<?> create(@RequestBody Board board, 
										@AuthenticationPrincipal CustomUserDetails userDetails) {
    	Member member = userDetails.getMember();
    	if (member != null) {
    		board.setMno(member.getMno());		
    	}
		int bno = boardService.add(board);
		return handleSuccess(Map.of("message", "등록되었습니다. bno: "+bno), HttpStatus.NO_CONTENT);
	}
    
    @PostMapping("/{mno}")
    public ResponseEntity<?> createTmp(@PathVariable Integer mno, @RequestBody Board board) {
    	board.setMno(mno);
    	int bno = boardService.add(board);
    	return handleSuccess(Map.of("message", "등록되었습니다. bno: "+bno), HttpStatus.NO_CONTENT);
    }
    
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required=false) String query, @RequestParam Integer mno) {
		List<Board> boardList;
		if (query != null && !query.equals("")) {
			boardList = boardService.boardFindWord(query);
		} else {
			boardList= boardService.getAllBoards(mno);
		}
		log.debug("boardList: {}", boardList);
		return handleSuccess(Map.of("boardList", boardList));
	}
    
    @GetMapping("/{bno}")
    public ResponseEntity<?> get(@PathVariable Integer bno, 
			@AuthenticationPrincipal CustomUserDetails userDetails) {
    	Member member = userDetails.getMember();
		Board board = boardService.getByBno(bno);
		if (member != null) {
			board.setLiked(boardService.getLike(bno, member.getMno()));
		}
		return handleSuccess(Map.of("board", board));
	}
    
    @GetMapping("/{bno}/{mno}")
    public ResponseEntity<?> getTmp(@PathVariable Integer bno, @PathVariable Integer mno) {
    	
    	Board board = boardService.getByBno(bno);
		board.setLiked(boardService.getLike(bno, mno));
		return handleSuccess(Map.of("board", board));
    }
    
    @PutMapping("/{bno}")
    public ResponseEntity<?> update(@PathVariable Integer bno, @RequestBody Board board) {
    	boardService.set(board);
		return handleSuccess(Map.of("message", "수정되었습니다. bno: "+bno), HttpStatus.NO_CONTENT);
	}
    
    @DeleteMapping("/{bno}")
    public ResponseEntity<?> delete(@PathVariable Integer bno) {
		boardService.remove(bno);
		return handleSuccess(Map.of("message", "삭제되었습니다."), HttpStatus.NO_CONTENT);
	}
	
    @PostMapping("/{bno}/likes")
    public ResponseEntity<?> createBoardLike(@PathVariable Integer bno, 
			@AuthenticationPrincipal CustomUserDetails userDetails) {
    	Member member = userDetails.getMember();
	    return handleSuccess(Map.of("likesCnt", boardService.increaseLike(bno, member.getMno())));
	}
    
    @PostMapping("/{bno}/likes/{mno}")
    public ResponseEntity<?> createBoardLikeTmp(@PathVariable Integer bno, @PathVariable Integer mno) { 
    	return handleSuccess(Map.of("likesCnt", boardService.increaseLike(bno, mno)));
    }
    
    @DeleteMapping("/{bno}/likes")
    public ResponseEntity<?> decreaseLike(@PathVariable Integer bno, 
			@AuthenticationPrincipal CustomUserDetails userDetails) {
    	Member member = userDetails.getMember();
    	return handleSuccess(Map.of("likesCnt", boardService.decreaseLike(bno, member.getMno())));
	}
    
    @DeleteMapping("/{bno}/likes/{mno}")
    public ResponseEntity<?> decreaseLikeTmp(@PathVariable Integer bno, @PathVariable Integer mno) { 
    	return handleSuccess(Map.of("likesCnt", boardService.decreaseLike(bno, mno)));
    }
    
    @PostMapping("/{bno}/comments")
    public ResponseEntity<?> addComment(@PathVariable int bno, @RequestBody Comment comment,
    		@AuthenticationPrincipal CustomUserDetails userDetails) {
    	Member member = userDetails.getMember();
    	if(member != null) {
    		comment.setMno(member.getMno());
    	}
    	comment.setBno(bno);
    	commentService.addComment(comment);
    	return handleSuccess(comment, HttpStatus.CREATED);
    }
    
    @GetMapping("/{bno}/comments")
    public ResponseEntity<?> getComments(@PathVariable int bno){
    	List<Comment> comments = commentService.getCommentsByBno(bno);
    	return handleSuccess(comments);
    }
    
    @PutMapping("/comments/{cno}")
    public ResponseEntity<?> updateComment(@PathVariable int cno, @RequestBody Comment comment){
    	comment.setCno(cno);
    	commentService.updateComment(comment);
    	return handleSuccess(comment);
    }
    
    @DeleteMapping("/comments/{cno}")
    public ResponseEntity<?> deleteComment(@PathVariable int cno){
    	commentService.deleteComment(cno);
    	return handleSuccess("삭제되었습니다.");
    }

}
