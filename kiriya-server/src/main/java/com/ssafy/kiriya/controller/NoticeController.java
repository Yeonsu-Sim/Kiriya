package com.ssafy.kiriya.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.dto.Notice;
import com.ssafy.kiriya.model.service.notice.NoticeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController implements ControllerHelper {
	private final NoticeService noticeService;
  
	/** 공지사항 목록 조회 */
	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam(required=false) String text) {
		List<Notice> noticeList;
		if (text != null && !text.equals("")) {
			noticeList = noticeService.noticeFindWord(text);
		} else {
			noticeList = noticeService.getAllNotices();
		}
		return handleSuccess(noticeList);
	}
	
	/** 공지사항 상세 조회 */
	@GetMapping("/{nno}")
	public ResponseEntity<?> get(@PathVariable int nno) {
		
		Notice notice = noticeService.getNoticeNno(nno);
		if (notice == null) {
            throw new IllegalArgumentException("존재하지 않는 공지입니다: nno=" + nno);
        }
        return handleSuccess(notice);
		
	}
	
	/** 공지사항 등록 */
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Notice notice
								, HttpSession session) {
		
		Member member = (Member) session.getAttribute("loggedIn");
		if (member != null) {
			notice.setMno(member.getMno());			
		}
		int nno = noticeService.add(notice);
		if (nno <= 0) {
            throw new IllegalArgumentException("공지사항 등록에 실패했습니다.");
        }
        Notice created = noticeService.getNoticeNno(notice.getNno());
        return handleSuccess(created, HttpStatus.CREATED);
	}
	
	/** 공지사항 수정 */
	@PutMapping("/{nno}")
	public ResponseEntity<?> update(@PathVariable int nno, @RequestBody Notice notice) {
		notice.setNno(nno);
		int result = noticeService.set(notice);
		if (result <= 0) {
            throw new IllegalArgumentException("존재하지 않는 공지입니다: nno=" + nno);
        }
		Notice updated = noticeService.getNoticeNno(nno);
        return handleSuccess(updated);
	}
	
	/** 공지사항 삭제 */
	@DeleteMapping("/{nno}")
	public ResponseEntity<?> delete(@PathVariable int nno) {
		int result = noticeService.remove(nno);
		if (result <= 0) {
            throw new IllegalArgumentException("존재하지 않는 공지입니다: nno=" + nno);
        }
        return handleSuccess("삭제되었습니다.");
	}
	
}
