<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" href="/css/route-view.css">
<div class="wrapper rounded p-4 shadow-lg bg-light">
	<a id="closeBtn" class="popup-close" data-popup="route-view-popup">X</a>
	<h4>경로 목록</h4>
	
	<!-- 내 목록 -->
	<div id="my-route-list" class="route-list p-2">
	</div>
	
	<!-- 그룹 목록 -->
	<div id="group-route-list" class="route-list p-2">
	</div>
</div>

<script src="/js/route-view.js"></script>
<script>
	const loggedInMno = `${loggedIn.mno}`;
</script>