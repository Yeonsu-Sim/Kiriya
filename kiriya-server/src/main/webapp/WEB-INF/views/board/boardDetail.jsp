<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="root" value="${pageContext.servletContext.contextPath}" />
    
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글</title>
  
  <link rel="stylesheet" href="/css/notice-board-view.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
      <%@ include file="../fragments/nav.jsp" %>
  </nav>
  
  <main>
	  <div class="main-container container">
	    <div id="postDetail">
	      <div class="post-title">${board.title}</div>
	      <div class="post-meta">
	        작성자: ${board.author} | 작성시간: ${board.createdAt} | 수정시간: ${board.updatedAt}
	      </div>
	      <div class="post-content">
	        ${board.content}
	      </div>
	      <br>
	      <div class="post-meta" style="margin-bottom:0px;">
	      	좋아요: <a id="likesCnt">${board.likes}</a>
	    	<button id="likeButton" onClick="like(this,${board.bno})" style="display:${board.liked?'none':'inline-block'};">좋아요</button>
	    	<button id="unlikeButton" onClick="unlike(this,${board.bno})" style="display:${board.liked?'inline-block':'none'};">좋아요 취소</button>
	      </div>
	    </div>
	    <a href="/board/boardList" class="btn btn-primary back-btn">목록으로</a>
	  </div>
  </main>
  
  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="../fragments/footer.jsp" %>
  </footer>
  
  <script src="/js/board-detail.js"></script>
  <script src="/js/common.js"></script>

</body>
<script>
	const loggedIn = `${loggedIn}`;
</script>
</html>
