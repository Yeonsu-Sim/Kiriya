package com.ssafy.kiriya.controller;


import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
import com.ssafy.kiriya.model.dto.Group;
import com.ssafy.kiriya.model.dto.GroupMember;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.dto.Trip;
import com.ssafy.kiriya.model.service.group.GroupService;
import com.ssafy.kiriya.model.service.trip.TripService;
import com.ssafy.kiriya.security.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController implements ControllerHelper {
	private final GroupService groupService;
	private final TripService tripService;

	/** 내 그룹 조회 */
	@GetMapping
	public ResponseEntity<?> getGroupsByMno(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		List<Group> groups = groupService.getGroupsByMember(loggedIn.getMno());
		return handleSuccess(groups);
	}

	/** 그룹 만들기 */
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Group group, @RequestParam List<String> emails, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		group.setMno(loggedIn.getMno());
		int gno = groupService.createGroup(group, emails);
		Group created = groupService.getGroupByGno(gno);

		return handleSuccess(created, HttpStatus.CREATED);
	}

	/** 그룹 단건 조회 */
	@GetMapping("/{gno}")
	public ResponseEntity<?> get(@PathVariable int gno, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		Group group = groupService.getGroupByGno(gno);
		List<GroupMember> members = groupService.getMembersByGroup(gno);
		return handleSuccess(Map.of("group", group, "members", members));
	}

	/** 그룹원 추가 */
	@PostMapping("/{gno}/members")
	public ResponseEntity<?> addMemberByEmail(@PathVariable int gno, @RequestParam String email, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != groupService.getGroupByGno(gno).getMno()) {
			throw new AccessDeniedException("권한이 없습니다.");
		}
		groupService.addMember(gno, email);
		return handleSuccess(Map.of("gno", gno, "email", email, "message", "멤버 추가 성공"));
	}
	
	/** 그룹원 삭제 */
	@DeleteMapping("/{gno}/members")
	public ResponseEntity<?> deleteMemberByEmail(@PathVariable int gno, @RequestParam String email, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != groupService.getGroupByGno(gno).getMno()) {
			throw new AccessDeniedException("권한이 없습니다.");
		}
		groupService.deleteMember(gno, email);
		return handleSuccess(Map.of("gno", gno, "email", email, "message", "멤버 삭제 성공"));
	}

	/** 그룹 수정 */
	@PutMapping("/{gno}")
	public ResponseEntity<?> update(@PathVariable int gno, @RequestBody Group group, @RequestParam List<String> emails, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != groupService.getGroupByGno(gno).getMno()) {
			throw new AccessDeniedException("권한이 없습니다.");
		}
		
		group.setGno(gno);
		group.setMno(loggedIn.getMno());
		
		groupService.updateGroup(group, emails);
		Group updated = groupService.getGroupByGno(gno);
		return handleSuccess(updated);
	}
	
	/** 그룹 삭제 */
	@Transactional
	@DeleteMapping("/{gno}")
	public ResponseEntity<?> delete(@PathVariable int gno, @AuthenticationPrincipal CustomUserDetails userDetails){
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		if (loggedIn.getMno() != groupService.getGroupByGno(gno).getMno()) {
			throw new AccessDeniedException("권한이 없습니다.");
		}
		
		groupService.deleteGroup(gno);
		return handleSuccess("그룹이 삭제되었습니다.");
	}
	
	/** 그룹별 여행목록 조회 */
	@GetMapping("/{gno}/trips")
    public ResponseEntity<?> getTripsByGroup(@PathVariable int gno, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Member loggedIn = userDetails.getMember();
		if (loggedIn == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		List<Trip> trips = tripService.getByGroup(gno);
        return handleSuccess(Map.of("trips", trips));
    }

	/** 그룹에 여행 추가 */
	@PostMapping("/{gno}/trips/{tno}")
	public ResponseEntity<?> addTripByCopy(@PathVariable Integer gno, @PathVariable Integer tno) {
		tripService.addTripByCopy(tno, gno);
		return handleSuccess("여행이 그룹에 공유되었습니다.", HttpStatus.CREATED);
	}
}
