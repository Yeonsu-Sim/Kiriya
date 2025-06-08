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
	    <form action="/board/boardUpdate" method="post">
			<input type="hidden" name="bno" value="${board.bno}"/>
			<div class="post-title">
			  <input type="text" id="title" name="title" value="${board.title}" required />
			</div>
			<div class="post-meta">
			  작성자: ${board.author} | 작성시간: ${board.createdAt} | 수정시간: ${board.updatedAt} | 좋아요: ${board.likes}
			</div>
			<div class="post-content">
			  <textarea id="content" rows="5" name="content" required>${board.content}</textarea>
			</div>
		    <div>
				<button type="submit" class="btn btn-primary back-btn">수정</button>
			    <a href="/board/boardList" class="btn btn-primary back-btn" >목록으로</a>
			    <a href="/board/boardDelete?bno=${board.bno}" class="btn btn-primary back-btn" >삭제하기</a>
		  	</div>
		 </form>
	   </div>
	  </div>
  </main>

  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="../fragments/footer.jsp" %>
  </footer>
  
  <script src="/js/notice-board-view.js"></script>
  
</body>
</html>
