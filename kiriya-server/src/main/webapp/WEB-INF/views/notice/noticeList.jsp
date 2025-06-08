<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="root" value="${pageContext.servletContext.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>자유게시판</title>
  <!-- index.css (사용 중인 CSS) -->
  <link rel="stylesheet" href="/css/index.css">
  <link rel="stylesheet" href="/css/notice-board.css">
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
	<jsp:include page="../fragments/nav.jsp" />
  </nav>
  
  <main>
	  <!-- 메인 컨테이너 -->
	  <div class="board-container">
	    <div class="board-title">공지사항</div>
	    <div class="search-form">
		  <form action="/notice/noticeList" method="get">
		    <input type="text" id="text" name="text" value="${text}" placeholder="검색어를 입력하세요...">
		    <button type="submit">검색하기</button>
		  </form>
		</div>
	    
	    <!-- 게시글 테이블 -->
	    <table>
	      <thead>
	        <tr>
	          <th>No</th>
	          <th>제목</th>
	          <th>글쓴이</th>
	          <th>작성시간</th>
	          <th>관리</th>
	        </tr>
	      </thead>
	      <tbody id="postList">
	        <!-- 게시글 목록이 동적으로 표시됩니다. -->
	        <c:set var = "count" value="1" />
	        <c:forEach var="notice" items="${noticeList}">
					<tr>
						<td>${count}</td>				
						<td><a href="/notice/noticeDetail?nno=${notice.nno}">${notice.title}</a></td>
						<td>${notice.author}</td>
						<td>${notice.createdAt}</td>
					    <td>
							<c:if test="${loggedIn.role.roleName eq '관리자'}">
						        <a href="/notice/noticeUpdateForm?nno=${notice.nno}">
						        	<button style ="border = none;">수정</button>
						        </a>
							</c:if>
					    </td>
					</tr>
					<c:set var="count" value="#{Integer.parseInt(count)+1}" />
				</c:forEach>
	      </tbody>
	    </table>
	
	    <!-- 페이지네이션 -->
	    <div class="pagination" id="pagination"></div>
	
	    <!-- 글쓰기 버튼 -->
	    <c:if test="${loggedIn.role.roleName eq '관리자'}">
		    <div>
		      <span class="write-btn" id="openFormBtn">글쓰기</span>
		    </div>
		
		    <!-- 글쓰기 폼 -->
		    <form class="write-form" id="postForm" action="/notice/noticeRegist" method="post">
		      <div>
		        <label for="title">제목</label>
		        <input type="text" id="title" name="title" required>
		      </div>
		      <div>
		        <label for="writer">글쓴이</label>
		        <input type="text" value = "${loggedIn.name}" readonly>
		      </div>
		      
		      <div>
		        <label for="content">내용</label>
		        <textarea id="content" rows="5" name="content" required></textarea>
		      </div>
		      <button type="submit">작성하기</button>
		    </form>
	    </c:if>
	  </div>
  </main>

  
  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
  	<%@ include file="../fragments/footer.jsp" %>
  </footer>

  <script src="/js/notice-board.js"></script>
  
  <script>
  	const loggedIn = `${loggedIn}`;
  </script>
  
</body>
</html>
