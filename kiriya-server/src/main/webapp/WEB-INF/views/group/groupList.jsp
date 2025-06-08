<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="root" value="${pageContext.servletContext.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 그룹</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- index.css (사용 중인 CSS) -->
<link rel="stylesheet" href="/css/index.css">
<link rel="stylesheet" href="/css/group.css">
</head>
<body>
	<!-- 네비게이션 바 -->
	<nav class="navbar navbar-expand-lg navbar-light bg-primary">
		<jsp:include page="../fragments/nav.jsp" />
	</nav>

	<main>
		<!-- 메인 컨테이너 -->
		<div class="group-container">
			<div class="group-title">내 그룹</div>
			<div class="search-form">
				<form action="/group/list" method="get">
					<input type="text" id="text" name="text" value="${text}"
						placeholder="검색어를 입력하세요...">
					<button type="submit">검색하기</button>
				</form>
			</div>

			<!-- 그룹 테이블 -->
			<table>
				<thead>
					<tr>
						<th>그룹 이름</th>
						<th>그룹 설명</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody id="groupList">
					<!-- 그룹 목록이 동적으로 표시됩니다. -->
					<c:forEach var="group" items="${groups}">
						<tr>
							<td><a href="/group/detail?gno=${group.gno}">${group.name}</a></td>
							<td>${group.description}</td>
							<td><c:if
									test="${group.mno eq loggedIn.mno or loggedIn.role.roleName eq '관리자'}">
									<a href="/group/update?gno=${group.gno}">
										<button style="">수정</button>
									</a>
								</c:if></td>

						</tr>
					</c:forEach>
				</tbody>
			</table>

			<!-- 페이지네이션 -->
			<div class="pagination" id="pagination"></div>

			<!-- 그룹 추가 버튼 -->
			<div>
				<span class="write-btn" id="openFormBtn">새 그룹 만들기</span>
			</div>
			
			 <!-- 1) 에러 메시지 -->
		    <c:if test="${not empty errorMsg}">
		      <div class="alert alert-danger">
		        ${errorMsg}
		      </div>
		    </c:if>

			<!-- 그룹 생성 폼 -->
			<form class="write-form" id="groupForm" action="/group/create"
				method="post" style="display: ${openForm ? 'block' : 'none'};">
				<div>
					<label for="name">그룹 이름</label> <input type="text" id="name"
						name="name" required>
				</div>

				<div>
					<label for="writer">그룹 생성자</label> <input type="text"
						value="${loggedIn.name}" readonly>
				</div>

				<div>
					<label for="description">그룹 설명</label>
					<textarea id="description" rows="5" name="description" required></textarea>
				</div>

				<div id="emailContainer">
					<label for="emails">친구 이메일 </label>
					<input type="email" style="flex:1 1 0; max-width:70%;"
						id="emails" name="emails" class="form-control email-input" placeholder="friend@example.com" required>
				<!-- 이메일 추가 버튼 -->
				<button type="button" id="addEmailBtn"
					class="btn btn-sm btn-outline-secondary">추가</button>
					<!-- 아래 엘리먼트에 에러 메시지 표시 -->
      				<div class="invalid-feedback"></div>
				</div>
				
				<div>
				<button type="submit" id="createBtn" class="btn btn-primary">그룹 만들기</button>
				</div>
			</form>
		</div>
	</main>

	<!-- 푸터 -->
	<footer class="bg-dark text-white py-3">
		<%@ include file="../fragments/footer.jsp"%>
	</footer>

	<script src="/js/group.js"></script>

</body>

</html>
