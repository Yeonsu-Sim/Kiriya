package com.ssafy.kiriya.controller;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Attraction;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.dto.Trip;
import com.ssafy.kiriya.model.service.attraction.AttractionService;
import com.ssafy.kiriya.model.service.member.MemberService;
import com.ssafy.kiriya.model.service.role.RoleService;
import com.ssafy.kiriya.model.service.trip.TripService;
import com.ssafy.kiriya.security.dto.CustomUserDetails;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController implements ControllerHelper {

	private final MemberService memberService;
	private final RoleService roleService;
	private final TripService tripService;
	private final AttractionService attractionService;

	/** 회원 가입 */
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody Member member, @RequestParam String passwordCheck) {
		try {
			Member created = memberService.signUp(member, passwordCheck);
			return handleSuccess(created, HttpStatus.CREATED);

		} catch (DuplicateKeyException e) {
			return handleFail(new IllegalArgumentException("이미 존재하는 이메일입니다."), HttpStatus.CONFLICT);

		}
	}

	/** 로그인 */
	/** SecurityFilter에서 잡아채서 로그인 로직 실행하므로 실제로 작동 안하는 코드임 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password,
			@RequestParam(required = false) String rememberMe, HttpServletResponse response) {
		Member member = memberService.login(email, password);
		Cookie cookie = new Cookie("rememberEmail", email);
		cookie.setPath("/");
		cookie.setMaxAge(rememberMe != null ? 7 * 24 * 60 * 60 : 0);
		response.addCookie(cookie);

		return handleSuccess(member);
	}

	/** 로그아웃 */
	@PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // remember-me 쿠키 삭제
        Cookie cookie = new Cookie("rememberEmail", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return handleSuccess("로그아웃 되었습니다");
    }

	/** 내 정보 페이지 */
	@GetMapping("/{mno}")
	public ResponseEntity<?> getMyInfo(@PathVariable int mno, @AuthenticationPrincipal CustomUserDetails userDetails) {
		// 세션 만료 이후 조회 및 다른 사람 정보 조회 불가능
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != mno) {
			throw new IllegalArgumentException("권한이 없습니다.");
		}
		Member member = memberService.get(mno);
		return handleSuccess(member);

	}

	/** 정보 수정 처리 */
	@PutMapping("/{mno}")
	public ResponseEntity<?> update(@PathVariable int mno, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Member member) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != mno) {
			throw new IllegalArgumentException("권한이 없습니다.");
		}

		member.setMno(mno);
		memberService.update(member);
		Member updated = memberService.get(mno);
		return handleSuccess(updated);

	}

	/** 회원 탈퇴 */
	@DeleteMapping("/{mno}")
	public ResponseEntity<?> delete(@PathVariable int mno, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != mno) {
			throw new IllegalArgumentException("권한이 없습니다.");
		}

		memberService.delete(mno);

		// 쿠키 삭제
		Cookie cookie = new Cookie("rememberEmail", null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return handleSuccess("탈퇴 완료되었습니다.");

	}

	/** 비밀번호 찾기(임시비밀번호 발급) */
	@PostMapping("/password/find")
	public ResponseEntity<?> findPassword(@RequestParam String name, @RequestParam String email) {
		boolean check = memberService.resetPassword(name, email);
		if (!check) {
			return handleFail(new IllegalArgumentException("이름 또는 이메일이 일치하지 않습니다."), HttpStatus.BAD_REQUEST);
		}
		return handleSuccess("이메일로 임시 비밀번호를 발송했습니다.");
	}

	/** 회원 조회(이메일) */
	@GetMapping("/exist")
	public ResponseEntity<?> existsByEmail(@RequestParam String email) {
		boolean exists = memberService.existsByEmail(email);
		return handleSuccess(Map.of("exists", exists));
	}

	/** 내 여행 조회 */
	@GetMapping("/{mno}/trips")
	public ResponseEntity<?> getTripsByMno(@PathVariable int mno, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails != null) {			
			Member loggedIn = userDetails.getMember();
			if (loggedIn == null) {
				throw new IllegalArgumentException("로그인이 필요합니다.");
			}
			if (loggedIn.getMno() != mno) {
				throw new IllegalArgumentException("권한이 없습니다.");
			}
		}

		List<Trip> trips = tripService.getByMno(mno);
		return handleSuccess(trips);
	}
	
	/** 내 버킷리스트 조회 */
	@GetMapping("/{mno}/attractions")
	public ResponseEntity<?> getAttractionsByMno(@PathVariable int mno, @AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails != null) {			
			Member loggedIn = userDetails.getMember();
			if (loggedIn == null) {
				throw new IllegalArgumentException("로그인이 필요합니다.");
			}
			if (loggedIn.getMno() != mno) {
				throw new IllegalArgumentException("권한이 없습니다.");
			}
		}
		List<Attraction> attractionList = null;
		attractionList = attractionService.getByMno(mno);
		for (Attraction attraction : attractionList) {
			attraction.setLiked(attractionService.getLike(attraction.getContentId(), mno));
		}
		return handleSuccess(attractionList);
		
	}
	
	/** 프로필 사진만 부분 수정 */
	@PostMapping(
			path = "/{mno}/avatar",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	public ResponseEntity<?> patchAvatar(@PathVariable int mno, @RequestPart("avatar") MultipartFile avatar){
		System.out.println("MemberController patchAvatar ========:");
		Member updated = memberService.patchAvatar(mno, avatar);
		return handleSuccess(updated);
	}
	
}