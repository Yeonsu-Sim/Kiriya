<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.servletContext.contextPath}"/>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>그룹 수정</title>
  <link href="${root}/css/group.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="../fragments/nav.jsp"/>
  <main class="group-container">
    <h2 class="group-title">그룹 정보 수정</h2>

    <c:if test="${not empty errorMsg}">
      <div class="alert alert-danger">${errorMsg}</div>
    </c:if>

    <form action="${root}/group/update" method="post" class="write-form" style="display:block;">
      <input type="hidden" name="gno" value="${group.gno}"/>

      <div class="mb-3">
        <label class="form-label">그룹 이름</label>
        <input type="text" name="name" class="form-control"
               value="${group.name}" required/>
      </div>
      <div class="mb-3">
        <label class="form-label">그룹 설명</label>
        <textarea name="description" class="form-control" rows="3" required>${group.description}</textarea>
      </div>
      <div class="mb-3">
       <label class="form-label">친구 이메일</label>
		  <div id="emailContainer">
		    <!-- 기존 멤버 이메일을 각각 하나씩 input으로 -->
		    <c:forEach var="email" items="${emails}">
		      <div class="email-row d-flex mb-2">
		        <input type="email"
		               name="emails"
		               class="form-control me-2"
		               value="${email}"
		               placeholder="friend@example.com"
		               required />
		        <button type="button"
		                class="btn btn-sm btn-outline-danger deleteEmailBtn">
		          삭제
		        </button>
		      </div>
		    </c:forEach>
		  </div>
		  <!-- 새로운 이메일 추가 버튼 -->
		  <button type="button"
		          id="addEmailBtn"
		          class="btn btn-sm btn-outline-secondary">
		    이메일 추가
		  </button>
	      </div>

      <button type="submit" class="btn btn-primary">수정하기</button>
      <a href="${root}/group/detail?gno=${group.gno}" class="btn btn-secondary ms-2">취소</a>
    </form>
  </main>
  <footer class="bg-dark text-white py-3">
		<%@ include file="../fragments/footer.jsp"%>
	</footer>

	<script src="/js/group.js"></script>
</body>
</html>
