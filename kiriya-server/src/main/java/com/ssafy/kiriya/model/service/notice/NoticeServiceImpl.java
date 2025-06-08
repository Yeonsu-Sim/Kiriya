package com.ssafy.kiriya.model.service.notice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import com.ssafy.kiriya.model.dao.NoticeDao;
import com.ssafy.kiriya.model.dto.Notice;
import com.ssafy.kiriya.model.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private final MemberService memberService;
	private final NoticeDao noticeDao;

	private static class Node implements Comparable<Node> {
		Notice notice = new Notice();
		int cost;

		public Node(Notice notice, int cost) {
			super();
			this.notice = notice;
			this.cost = cost;
		}

		@Override
		public int compareTo(Node o) {
			if (this.cost == o.cost) {
				// 날짜 비교 - 최신 날짜가 먼저 오도록 (내림차순)
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime thisDate = LocalDateTime.parse(this.notice.getCreatedAt(), formatter);
				LocalDateTime otherDate = LocalDateTime.parse(o.notice.getCreatedAt(), formatter);
				return otherDate.compareTo(thisDate); // 내림차순
			}
			return Integer.compare(o.cost, this.cost);
		}

	}

	@Override
	public int add(Notice notice) {
		if (memberService.get(notice.getMno()).getRole().getRoleName().equals("ADMIN")) {
			return noticeDao.add(notice);
		} else {
			throw new IllegalArgumentException("등록 권한이 없습니다.");
		}
	}

	@Override
	public List<Notice> getBytitle(String title) {
		List<Notice> notice = noticeDao.getBytitle(title);
		return notice;
	}

	@Override
	// 공지사항 전체 조회
	public List<Notice> getAllNotices() {
		List<Notice> notice = noticeDao.getAllNotices();
		return notice;
	}

	@Override
	public int set(Notice notice) {
		return noticeDao.set(notice);

	}

	@Override
	public int remove(int nno) {
		return noticeDao.remove(nno);
	}

	@Override
	public Notice getNoticeNno(int nno) {
		Notice notice = noticeDao.getByNno(nno);
		return notice;
	}

	@Override
	public List<Notice> noticeFindWord(String text) {
		List<Notice> notice = noticeDao.getAllNotices();
		List<Notice> newNotice = new ArrayList<>();
		String[] texts = text.split(" ");
		PriorityQueue<Node> pq = new PriorityQueue<>();

		for (int i = 0; i < notice.size(); i++) {
			int num = 0;
			for (String s : texts) {
				int k = findWord(notice.get(i).getTitle(), s);
				int k2 = findWord(notice.get(i).getContent(), s);
				if (k == 1 || k2 == 1) {
					num += 1;
				}
			}
			if (num > 0) {
				pq.add(new Node(notice.get(i), num));
			}
		}
		while (!pq.isEmpty()) {
			Node now = pq.poll();
			newNotice.add(now.notice);
		}
		return newNotice;

	}

	static final int d = 256; // 알파벳 개수 (ASCII 기준)
	static final int q = 101; // 소수 (mod 연산용)

	public int findWord(String text, String pattern) {
		if (text == null || pattern == null)
			return 0;

		int n = text.length();
		int m = pattern.length();

		// 패턴이 텍스트보다 길면 탐색 불가
		if (m > n || m == 0)
			return 0;

		int i, j;
		int p = 0;
		int t = 0;
		int h = 1;

		// h = pow(d, m-1) % q
		for (i = 0; i < m - 1; i++)
			h = (h * d) % q;

		for (i = 0; i < m; i++) {
			p = (d * p + pattern.charAt(i)) % q;
			t = (d * t + text.charAt(i)) % q;
		}

		for (i = 0; i <= n - m; i++) {
			if (p == t) {
				for (j = 0; j < m; j++) {
					if (text.charAt(i + j) != pattern.charAt(j))
						break;
				}
				if (j == m) {
					return 1;
				}
			}

			if (i < n - m) {
				t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
				if (t < 0)
					t += q;
			}
		}

		return 0;
	}

}
