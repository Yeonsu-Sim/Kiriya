package com.ssafy.kiriya.model.service.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.kiriya.model.dao.BoardDao;
import com.ssafy.kiriya.model.dto.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

	private final BoardDao boardDao;
	
	private static class Node implements Comparable<Node>{
		Board board = new Board();
		int cost;
		public Node(Board board, int cost) {
			super();
			this.board = board;
			this.cost = cost;
		}
		
		@Override
		public int compareTo(Node o) {
		    if (this.cost == o.cost) {
		        if (this.board.getLikes() == o.board.getLikes()) {
		            // 날짜 비교 - 최신 날짜가 먼저 오도록 (내림차순)
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		            LocalDateTime thisDate = LocalDateTime.parse(this.board.getCreatedAt(), formatter);
		            LocalDateTime otherDate = LocalDateTime.parse(o.board.getCreatedAt(), formatter);
		            return otherDate.compareTo(thisDate); // 내림차순
		        }
		        return Integer.compare(o.board.getLikes(), this.board.getLikes());
		    }
		    return Integer.compare(o.cost, this.cost);
		}
		
	}
	
	@Override
	public int add(Board board) {
		// 1. 게시글 추가 
		int result = boardDao.add(board);
		int bno = board.getBno();
		if(result==0) return 0;
		
		// 2. 이미지 여러 장 추가
		List<String> imageUrls = board.getImageUrls();
		if(imageUrls != null && !imageUrls.isEmpty()) {
			int seq = 1;
			for (String url : imageUrls) {
				boardDao.addImage(bno, url, seq++);
			}
		}
		
		// 3. 태그 여러 개 추가
		List<String> tags = board.getTags();
		if(tags != null && !tags.isEmpty()) {
			for (String tagName : tags) {
			    Integer tagId = boardDao.findTagByName(tagName);
			    if (tagId == null) {
			        boardDao.addTag(tagName); // insert
			        tagId = boardDao.findTagByName(tagName); // 다시 select
			    }
			    if (tagId == null) {
			        log.error("태그 저장 후에도 tagId를 찾을 수 없습니다: {}", tagName);
			        continue; // 또는 throw new RuntimeException(...) 으로 강제 종료
			    }
			    boardDao.addBoardTag(bno, tagId);
			}

		}
		return 1;
	}

	@Override
	public List<Board> getBytitle(String title) {
		return boardDao.getBytitle(title);
	}

	@Override
	// 게시글 전체 조회 (이미지/태그 포함) 
	public List<Board> getAllBoards(int mno) {
		List<Board> boards = boardDao.getAllBoards(mno);
		for (Board board : boards) {
			int bno = board.getBno();
			List<String> imageUrls = boardDao.getImagesByBno(bno);
			board.setImageUrls(imageUrls);
			List<String> tags = boardDao.getTagsByBno(bno);
			board.setTags(tags);
		}
		return boards;
	}

	@Override
	public int set(Board board) {
		int result = boardDao.set(board);
		if (result < 1) throw new IllegalArgumentException("수정에 실패했습니다.");
		int bno = board.getBno();
		
		// 1. 기존 이미지/태그 삭제
        boardDao.removeImagesByBno(bno);
        boardDao.removeBoardTagsByBno(bno);

        // 2. 새 이미지/태그 insert
        List<String> imageUrls = board.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            int seq = 1;
            for (String url : imageUrls) {
                boardDao.addImage(bno, url, seq++);
            }
        }
        List<String> tags = board.getTags();
        if (tags != null && !tags.isEmpty()) {
            for (String tagName : tags) {
                Integer tagId = boardDao.findTagByName(tagName);
                if (tagId == null) {
                    boardDao.addTag(tagName);
                    tagId = boardDao.findTagByName(tagName);
                }
                if (tagId != null) {
                    boardDao.addBoardTag(bno, tagId);
                }
            }
        }
        return 1;
	}
	
	@Override
	public boolean getLike(int bno, int mno) {
		return boardDao.getLike(bno, mno) > 0; 
	}
	
	@Override
	@Transactional
	public int increaseLike(int bno, int mno) {
		int result = boardDao.increaseLike(bno, mno);
		if (result != 1) {
			throw new IllegalArgumentException("좋아요를 누를 수 없습니다.");
		}
		return boardDao.getAllLikes(bno);
	}
	
	@Override
	public int decreaseLike(int bno, int mno) {
		int result = boardDao.decreaseLike(bno, mno);
		return boardDao.getAllLikes(bno);
	}
	
	@Override
	@Transactional
	public int remove(int bno) {
		boardDao.nullLike(bno);
		return boardDao.remove(bno);
	}

	@Override
	public Board getByBno(int bno) {
		Board board = boardDao.getByBno(bno);
		if (board == null) {
			throw new IllegalArgumentException("조회된 게시글이 없습니다. bno: "+bno);
		}
		board.setImageUrls(boardDao.getImagesByBno(bno));
        board.setTags(boardDao.getTagsByBno(bno));
		return board;
	}

	@Override
	public List<Board> boardFindWord(String text) {
		System.out.println(text);
		int mno = 0; // 임시 mno
		List<Board> board = getAllBoards(mno);
		List<Board> newBoards = new ArrayList<>();
		String[] texts = text.split(" ");  // 키워드들을 담음 이게 패턴들임
//			Map<String, Integer> map = new HashMap<>();
		PriorityQueue<Node> pq = new PriorityQueue<>();  // 여기에 가중치 별로 담아서  다시 board에 담아줄거임
		
		for(int i = 0; i<board.size(); i++) {
			int num = 0;
			for(String s : texts) {
				int k = findWord(board.get(i).getTitle(), s);
				int k2 = findWord(board.get(i).getContent(), s);
//					System.out.println("본문: " + board.get(i).getTitle());
//					System.out.println("검색어: " + s);
//					System.out.println("결과: " + k);
				
				if(k==1||k2==1) {
					System.out.println(num);
					num += 1;  // 가중치들 더함
				}
				
			}
			System.out.println("num:"+num);
			if(num>0) {
				pq.add(new Node(board.get(i), num));
				
			}
			
		}
		while(!pq.isEmpty()) {
			Node now = pq.poll();
			newBoards.add(now.board);
		}
		return newBoards;
	}
	
	static final int d = 256; // 알파벳 개수 (ASCII 기준)
	static final int q = 101; // 소수 (mod 연산용)
    
	public int findWord(String text, String pattern) {
	    if (text == null || pattern == null) return 0;

	    int n = text.length();
	    int m = pattern.length();

	    // 패턴이 텍스트보다 길면 탐색 불가
	    if (m > n || m == 0) return 0;

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
	            if (t < 0) t += q;
	        }
	    }

	    return 0;
	}

}
