package com.ssafy.kiriya.model.service.member;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.kiriya.model.dto.Member;

public interface MemberService {
    /**
     * 회원 가입 처리
     * @param member 가입할 회원 정보
     * @param passwordCheck 비밀번호 확인
     * @throws IllegalArgumentException 비즈니스 검증 실패 시
     */
    Member signUp(Member member, String passwordCheck);

    /**
     * 로그인 처리
     * @param email 이메일
     * @param password 비밀번호
     * @return 인증된 Member 객체
     * @throws IllegalArgumentException 인증 실패 시
     */
    Member login(String email, String password);
    
    public Member get(int mno);
	
	boolean existsByEmail(String email);

    /**
     * 회원 정보 수정 처리
     * @param member 수정할 회원 객체
     * @throws IllegalArgumentException 검증 또는 수정 중 오류 시
     */
    void update(Member member);

    /** 회원 프로필 변경 처리 */
    Member patchAvatar(int mno, MultipartFile avatar);
    
    /**
     * 회원 탈퇴 처리
     * @param mno 탈퇴할 회원 번호
     * @throws IllegalArgumentException 탈퇴 중 오류 시
     */
    void delete(int mno);
    
    boolean resetPassword(String name, String email);
    
    Member selectByEmail(String email);

}