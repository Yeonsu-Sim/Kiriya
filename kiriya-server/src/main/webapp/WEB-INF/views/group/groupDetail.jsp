<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="root" value="${pageContext.servletContext.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>그룹 상세</title>
  <!-- 기존에 쓰시던 board-detail CSS 재사용 -->
  <link rel="stylesheet" href="${root}/css/group-view.css">
  <!-- 팝업용 CSS -->
  <link rel="stylesheet" href="/css/popup.css">
  <link rel="stylesheet" href="/css/route-save.css">
  
  <style>
  .popup {
	  position: fixed;
	  top: 0;
	  left: 0;
	  width: 100vw;      /* 화면 전체 너비 */
	  height: 100vh;     /* 화면 전체 높이 */
	  display: flex;     /* d-flex 대신 여기서 지정해도 됩니다 */
	  justify-content: center; /* 가로 중앙 */
	  align-items: center;     /* 세로 중앙 */
	  background-color: rgba(0, 0, 0, 0.5); /* 반투명 어둡게 처리 */
	  z-index: 1050;     /* 모달 위에 뜨도록 충분히 큰 값 */
	}
	.plan-list ul {
	  list-style: none;
	  margin: 0 0 1rem 0;       /* 아래쪽 여백으로 다른 ul과 분리 */
	  padding: 0.5rem;         /* 안쪽 여백 확보 */
	  border: 1px solid #ddd;  /* 외곽 테두리 */
	  border-radius: 4px;      /* 모서리 둥글게 */
	  background: #fafafa;     /* 선택사항: 약간 연한 배경 */
	}
  </style>
</head>
<body>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-expand-lg navbar-light bg-primary">
    <jsp:include page="../fragments/nav.jsp"/>
  </nav>

  <main>
    <div class="main-container">
      <!-- 그룹 기본 정보 -->
      <div class="group-title">${group.name}</div>
      <div class="group-meta">
        설명: ${group.description}
      </div>
      <div class="group-meta">
      생성자: 
       <c:choose>
	    <c:when test="${not empty members}">
	      <c:forEach var="m" items="${members}">
	        <c:if test="${m.mno == group.mno}">
	          ${m.memberName}
	        </c:if>
	      </c:forEach>
	    </c:when>
	    <c:otherwise>
	      (알 수 없음)
	    </c:otherwise>
	  </c:choose>
      </div>

      <!-- 그룹원 리스트 -->
      <div class="group-container">
        <h4>그룹원</h4>
        <table>
          <thead>
            <tr>
              <th>이름</th>
              <th>역할</th>
              <th>가입일</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty members}">
                <c:forEach var="m" items="${members}">
                  <tr>
                    <td>${m.memberName}</td>
                    <td>
                      <c:choose>
                        <c:when test="${m.mno == group.mno}">
                          방장
                        </c:when>
                        <c:otherwise>
                          멤버
                        </c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      ${m.joinedAt}
                    </td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr>
                  <td colspan="3" class="text-center">
                    아직 초대된 멤버가 없습니다
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
      
       <!-- 여행 추가 버튼 -->
      <div class="mt-4">
        <button type="button"
                class="btn btn-primary"
                data-popup-open="route-save-popup">
          여행 추가
        </button>
      </div>

      <!-- 팝업 (초기 상태 hidden) -->
      <div id="route-save-popup" class="d-flex popup hide">
        <div class="wrapper rounded p-4 shadow-lg bg-light">
          <a id="closeBtn" class="popup-close" data-popup="route-save-popup">×</a>
          <h5>내 여행 목록</h5>
          <div id="trip-container" class="w-100 p-2">
            <!-- group-trip.js가 AJAX로 채워넣습니다 -->
          </div>
        </div>
      </div>

      <!-- 그룹 여행 목록 -->
      <div class="mt-5">
        <h4>그룹 여행 목록</h4>
        <div id="group-trip-items">
          <!-- 초기 로드 시 group-trip.js가 AJAX로 채웁니다 -->
        </div>
      </div>

      <!-- 뒤로 가기 -->
      <a href="${root}/group/list" class="btn btn-primary back-btn mt-4">
        목록으로
      </a>
    </div>
  </main>

  <!-- 푸터 -->
  <footer class="bg-dark text-white py-3">
    <jsp:include page="../fragments/footer.jsp"/>
  </footer>
  
  <script>
	  const groupGno = `${group.gno}`;
	  console.log(groupGno);
	  const loggedInMno = `${loggedIn.mno}`;
  </script>
  <script src="/js/group-trip.js"></script>
  
</body>
</html>
