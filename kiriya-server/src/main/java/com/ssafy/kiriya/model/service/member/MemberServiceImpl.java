package com.ssafy.kiriya.model.service.member;


import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.kiriya.model.dao.MemberDao;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.s3.service.S3ImageService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member signUp(Member member, String passwordCheck) {
        if (!member.getPassword().equals(passwordCheck)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        // 평문 → BCrypt 암호문으로 변환
        member.setPassword(passwordEncoder.encode(member.getPassword()));

	    memberDao.add(member);
	    return member;
    }

    @Override
    public Member login(String email, String password) {
        Member member = memberDao.getByEmail(email);
        if (member != null && member.getPassword().equals(password)) {
        	return member;
        }
        throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
    }
    
    @Override
	public Member get(int mno) {
		Member member = memberDao.get(mno);
		if (member == null) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		}
		return member;
	}
    
    @Override
    public boolean existsByEmail(String email) {
    	return memberDao.getByEmail(email) != null;
    }


    @Override
    public void update(Member member) {
    	Member existing = memberDao.get(member.getMno());
    	if(member.getPassword() == null || member.getPassword().isEmpty()) {
    		member.setPassword(existing.getPassword());
    	} else {
    		member.setPassword(passwordEncoder.encode(member.getPassword()));
    	}
        int updated = memberDao.set(member);
        if (updated == 0) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
    }
    
    private final S3ImageService s3ImageService;
    
    @Override
    public Member patchAvatar(int mno, MultipartFile avatar) {
    	Member existing = memberDao.get(mno);
    	if(existing.getProfileUrl()!=null) {
    		try {
    			s3ImageService.deleteImageFromS3(existing.getProfileUrl());
    		} catch(Exception e) {
    			log.warn("▶▶▶ failed to delete old image (ignored): {}", e.getMessage());
            }
        }

    	String imageUrl = s3ImageService.upload(avatar);
    	memberDao.updateAvatar(mno, imageUrl);
    	return memberDao.get(mno);
    }

    @Override
    public void delete(int mno) {
    	memberDao.remove(mno);
    }

    /**
     * 임시 비밀번호 발급 메서드 
     */
	
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * 사용자 정보가 일치하면,
     * 10자리 임시 비밀번호를 발급 후 비밀번호 업데이트 
     */
	@Override
	public boolean resetPassword(String name, String email) {
			Member member = memberDao.getByEmail(email);
			if(member==null || !member.getName().equals(name)) {
				return false;
			}
			
			String tmpPw = generateRandomPassword(10);
			System.out.println();
			System.out.println("=====================변경된 비밀번호==================== : " + tmpPw);
			System.out.println();
			String encoded = passwordEncoder.encode(tmpPw);
			member.setPassword(encoded);
			memberDao.set(member);

			try {
	            sendTmpPwEmail(email, member.getName(), tmpPw);
	        } catch (MessagingException e) {
	            // 서버 오류로 메일 발송이 안된 경우
	            throw new IllegalStateException("임시 비밀번호 이메일 전송 중 오류가 발생했습니다.", e);
	        }
			return true;
	}

	/**
	 * 임시 비밀번호를 사용자의 이메일에 전송하는 메서드 
	 * @param to
	 * @param name
	 * @param tmpPw
	 */
	private void sendTmpPwEmail(String to, String name, String tmpPw) throws MessagingException {
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
		helper.setTo(to);
		helper.setSubject("[KIRIYA] 임시 비밀번호 안내");
		String txt = String.format(
				"%s 님,\n\n임시 비밀번호: %s\n\n로그인 후 반드시 비밀번호를 변경해 주세요.",
				name, tmpPw
				);
		helper.setText(txt);
		mailSender.send(msg);
	}

	/**
	 * 임시 비밀번호 10자리 생성하는 메서드 
	 * @param length
	 * @return
	 */
	private String generateRandomPassword(int length) {
		SecureRandom rnd = new SecureRandom();
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder(length);
		for(int i=0; i<length; i++) {
			sb.append(chars.charAt(rnd.nextInt(chars.length())));
		}
		return sb.toString();
	}

	@Override
	public Member selectByEmail(String email) {
		return memberDao.selectByEmail(email);
	}


}