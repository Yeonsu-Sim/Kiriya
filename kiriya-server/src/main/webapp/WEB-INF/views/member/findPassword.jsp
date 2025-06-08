<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.servletContext.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>비밀번호 찾기</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!-- 로그인 페이지와 동일한 CSS 사용 -->
  <link rel="stylesheet" href="${root}/css/auth.css">
  <style>
    :root {
      --max-height: 600px; /* 기존 420px에서 증가 */
      --max-width: 500px
      
    }
    /* 오버레이 영역은 필요 없으므로 숨김 처리 */
    .container__overlay {
      display: none;
    }
    /* find password 폼을 컨테이너 중앙에 배치 */
    .container--signin {
      left: 50% !important;
      transform: translateX(-50%) !important;
      width: 100% !important;
    }
  </style>
</head>
<body>
  <div class="container">
    <!-- 비밀번호 찾기 폼 -->
    <div class="container__form container--signin">
      <form id="findPasswordForm" class="form" action="${root}/member/findPassword" method="post">
        <h2 class="form__title">비밀번호 찾기</h2>
        <div class="form-group">
          <label for="name">이름</label>
          <input type="text" id="name" name="name" placeholder="Name" class="input" required />
        </div>
        <div class="form-group">
          <label for="email">이메일</label>
          <input type="email" id="email" name="email" placeholder="Email" class="input" required />
        </div>
        <button type="submit" class="btn btn-submit">임시 비밀번호 발급</button>
        <br>
        <div class="login-link">
          <a href="${root}/member/authForm?login=true">로그인 페이지로 이동</a>
        </div>
      </form>
    </div>
  </div>
  
  <!-- <script src="${root}/js/find_password.js"></script> -->
</body>
</html>
