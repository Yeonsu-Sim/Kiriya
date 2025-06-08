<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/css/route-save-form.css">
<div class="wrapper rounded p-4 shadow-lg bg-white">
	<a id="closeBtn" class="popup-close" data-popup="route-save-form-popup">X</a>
	<h4>일정 저장</h4>
	
	<!-- 일정 저장 폼 -->
	<form id="planForm" class="mb-2">
	  <div>
	    <label for="title">제목</label>
	    <input type="text" id="planTitle" name="title" required></input>
	  </div>
	  <div>
	    <label for="description">설명</label>
	    <textarea id="planDescription" rows="2" name="description"></textarea>
	  </div>
	  <div>
	    <label for="startDate">시작 날짜</label>
	    <input type="datetime-local" id="planStartDate" name="startDate"></input>
	  </div>
	  <div>
	    <label for="endDate">종료 날짜</label>
	    <input type="datetime-local" id="planEndDate" name="endDate"></input>
	  </div>
	  <button type="submit" id="submitPlanFormBtn" class="btn btn-primary btn-sm m-2 ms-1">완료</button>
	</form>
</div>
<script src="/js/route-save-form.js"></script>