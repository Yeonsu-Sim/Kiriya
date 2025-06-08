<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>관광지도</title>
    <link rel="stylesheet" href="/css/map.css">
    <link rel="stylesheet" href="/css/popup.css">
    <link rel="stylesheet" href="/css/tab.css">
    <!-- Bootstrap CSS 추가 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome 추가 -->
    <script src="https://kit.fontawesome.com/ee3f3b0c65.js" crossorigin="anonymous"></script>
    <style>
    	body {
    		width: 100%;
    		height: 100%;
    	}
    </style>
</head>
<body>

    <!-- 네비게이션 바 -->
    <nav class="navbar navbar-expand-lg navbar-light bg-primary">
        <%@ include file="../fragments/nav.jsp" %>
    </nav>
	
    <!-- 메인 컨텐츠 -->
    <main class="d-flex overflow-hidden">
		<!-- 경로 저장 팝업 -->
		<div class="d-flex popup hide" id="route-save-popup">
			<%@include file="route-save.jsp" %>
		</div>
		<!-- 경로 저장 폼 팝업 -->
		<div class="d-flex popup hide" id="route-save-form-popup">
			<%@include file="route-save-form.jsp" %>
		</div>
		
		<!-- 경로 불러오기 팝업 -->
		<div class="d-flex popup hide" id="route-view-popup">
			<%@include file="route-view.jsp" %>
		</div>
	    
        <div class="d-flex position-relative">
            <div class="w-100 position-relative center">
            
                <!-- 지역 선택 필터 -->
				<div class="filters d-flex flex-wrap justify-content-center gap-3 my-3 me-5">
				    <select id="areaCode" class="form-select" style="width: 200px;">
				        <option value="" selected disabled>시도 선택</option>
				    </select>
				
				    <select id="sigunguCode" class="form-select" style="width: 200px;">
				        <option value="" selected disabled>시군구 선택</option>
				    </select>
				
				    <select id="contentType" class="form-select" style="width: 200px;">
				        <option value="" selected disabled>관광타입 선택</option>
				        <c:forEach items="${contentTypeList}" var="content">
				            <option value="${content.code}">${content.name}</option>
				        </c:forEach>
				    </select>
				    <button id="btn_trip_search" class="btn btn-primary px-4">조회</button>
				</div>
                
                <!-- 지도 영역 -->
                <div>
                    <div id="map"></div>
                    <div id="popup" class='h-100 d-flex align-items-center position-absolute z-3 top-0'>
                        
                        <!-- 관광지 상세 정보 -->
                        <section id="tour-info" class="h-100 d-flex flex-row">
                        	<div>
                        		<div id="tour-tab-container" class="d-flex flex-row justify-content-between">
		                    		<a id="info-tab" class="tour-tab active" data-pane="info-list">검색</a>
		                    		<a id="bucket-tab" class="tour-tab" data-pane="bucket-list">버킷리스트</a>
		                    	</div>
	                        	<div id="info-list" class="tour-tab-pane active h-100 p-3 pe-auto bg-light shadow overflow-auto">
									<input id="trip-keyword-input"placeholder="검색어를 입력해 주세요." class="form-control-plaintext bg-white shadow-sm p-2 mb-3">
									<div id="info-container" class="rounded align-items-center d-flex flex-column my-2"></div>
	                        	</div>
								<div id="bucket-list" class="tour-tab-pane h-100 p-3 pe-auto bg-light shadow overflow-auto">
									<div id="bucket-container" class="rounded align-items-center d-flex flex-column my-2"></div>
								</div>
                        	</div>
	                        <div id="info-detail" class="bg-light shadow align-items-center d-flex flex-column" >
	                        	<div id="detail-container" class="rounded overflow-auto align-items-center d-flex flex-column w-100 p-3 pe-auto" >
	                        	</div>
	                        </div>
	                        
                        </section>
                        <!-- ✅ 토글 버튼 추가 -->
                        <button id="toggle-info-btn" class="btn btn-primary pe-auto">
                            <
                        </button>
                    </div>
                    <!-- 경로 추천 사이드 -->
                    <div id="route-info" class="d-flex flex-column border rounded me-5">
                    	<div id="route-tab-container" class="d-flex flex-row justify-content-between">
                    		<a id="make-route-tab" class="tab active" data-pane="make-route">경로 만들기</a>
                    		<a id="view-route-tab" class="tab" data-pane="view-route">경로 보기</a>
                    	</div>
                    	<div id="route-tab-pane-container" class="w-100 h-100 d-flex flex-column bg-light shadow rounded me-5">
	                    	<div id="make-route" class="tab-pane active h-100">
		                    	<div class="d-flex flex-row justify-content-between">
			                    	<button id="getRouteButton" class="btn btn-outline-primary btn-sm m-2 me-1">경로 추천받기</button>
			                    	<button id="saveRouteButton" class="popup-open btn btn-secondary btn-sm m-2 ms-1" data-popup="route-save-popup">경로 저장</button>
		                    	</div>
		                    	<div id="route-container" class="overflow-auto d-flex flex-column p-2 align-items-center z-3"></div>
	                    	</div>
	                    	<div id="view-route" class="tab-pane h-100">
		                    	<div class="d-flex flex-row justify-content-between">
		                    		<button id="viewRouteButton" class="popup-open btn btn-outline-primary btn-sm m-2 me-1" data-popup="route-view-popup">경로 불러오기</button>
		                    		<button id="modifyOnButton" class="btn btn-danger btn-sm m-2 me-1">수정</button>
		                    		<button id="updateRouteButton" class="btn btn-primary btn-sm m-2 ms-1">완료</button>
		                    		<button id="modifyOffButton" class="btn btn-outline-secondary btn-sm m-2 ms-1">취소</button>
	                    		</div>
	                    		<div id="view-route-container" class="overflow-auto d-flex flex-column p-2 align-items-center z-3"></div>
	                    	</div>
	                    	<div class="d-flex flex-row justify-content-end">
	                    		<button id="deleteAllRouteButton" class="btn btn-outline-danger btn-sm m-2 ms-1" data-popup="route-save-popup">모두 삭제</button>
	                    	</div>
                    	</div>
                    </div>
                    
                </div>
            </div>
        </div>

        <!-- 토스트 컨테이너 -->
        <div class="toast-container position-fixed bottom-0 end-0 p-3"></div>
    </main>

    <!--서비스 ID 설정-->
    <script type = "text/javascript" src = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/javascriptAuth?consumer_key=97c19896de3843c9bf5e" ></script>
    <script src="/js/common.js"></script>
    <script src="/js/filter.js"></script>
    <script src="/js/kostat.js"></script>
    <script src="/js/enjoytrip.js"></script>
    <script src="/js/map.js"></script>
    <script src="/js/popup.js"></script>
    <script src="/js/tab.js"></script>
    
    <script>
		//alert 메세지 출력
		const alertMsg = `${alertMsg}`;
		if (alertMsg) {
			alert(alertMsg);
		}
	</script>
	
	<script>
		 const key_vworld = `${key_vworld}`;
		 const key_sgis_service_id = `${key_sgis_service_id}`;  // 서비스 id
		 const key_sgis_security = `${key_sgis_security}`; // 보안 key
		 const key_data = `${key_data}`;
		 const loggedIn =`${loggedIn}`;
	</script>
    
    <script>
        const init = async () => {
        	await getFilters();
        	
        	if (await updateFilter()) {
        		searchAttractions();
        	} else {
	        	updateMap([
	                {
	                    address: "서울특별시 강남구 테헤란로 212",
	                    utmk: await getCoords("서울특별시 강남구 테헤란로 212"),
	                    label: "멀티캠퍼스",
	                },
	            ]);
        	}
            //{ address: address, utmk: getCoords(address), label: aptNm }
        };
        init();
        if (loggedIn) showBucketList();
    </script>
    
    <script>
        document.getElementById("toggle-info-btn").addEventListener("click", function (event) {
        	const target = event.currentTarget;
        	const toggle = document.querySelector("#tour-info"); 
        	if (toggle.classList.contains('hide')) {
        		target.innerHTML = '<';
        	} else {
        		target.innerHTML = '>';
        	}
            toggle.classList.toggle('hide');
        });
    </script>
</body>

</html>
