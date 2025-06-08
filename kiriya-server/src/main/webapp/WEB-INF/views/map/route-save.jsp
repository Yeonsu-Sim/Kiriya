<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/css/route-save.css">
<div class="wrapper rounded p-4 shadow-lg bg-light">
	<a id="closeBtn" class="popup-close" data-popup="route-save-popup">X</a>
	<h4>여행 목록</h4>
	
	<!-- 여행 추가 폼 -->
	<div>
	   <button class="btn btn-outline-primary btn-sm my-2 ms-1" id="openFormBtn">여행 추가</button>
	</div>
	<form id="tripForm" class="mb-2">
	  <div>
	    <label for="title">제목</label>
	    <input type="text" id="title" name="title" required></input>
	  </div>
	  <div>
	    <label for="description">설명</label>
	    <textarea id="description" rows="2" name="description"></textarea>
	  </div>
	  <div>
	    <label for="startDate">시작 날짜</label>
	    <input type="datetime-local" id="startDate" name="startDate"></input>
	  </div>
	  <div>
	    <label for="endDate">종료 날짜</label>
	    <input type="datetime-local" id="endDate" name="endDate"></input>
	  </div>
	  <button type="submit" id="submitFormBtn" class="btn btn-primary btn-sm m-2 ms-1">완료</button>
	  <input type="button" id="closeFormBtn" class="btn btn-outline-secondary btn-sm m-2 ms-1" value="취소">
	</form>
	
	<!-- 여행 목록 -->
	<div id="trip-container" class="w-100 p-2">
	</div>
</div>

<script src="/js/route-save.js"></script>
<script>
	const loggedInMno = `${loggedIn.mno}`;
</script>