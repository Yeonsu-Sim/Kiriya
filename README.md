# FW 관통프로젝트 13팀 [ENJOY_TRIP]

## 📁 프로젝트명 : 끼리야 🐘

> 끼리끼리 모여 그룹원들과 여행 스케줄을 공유하는 여행 가이드 웹 서비스

## 📌 프로젝트 소개

끼리야는 친한 사람들과 함께 여행 계획을 손쉽게 수립하고, 관광 정보를 빠르게 얻으며, 그룹 단위의 일정 관리까지 지원하는 종합 여행 서비스 플랫폼입니다. 팀 여행 계획부터 개인 여행 관리까지, 여행지 탐색과 일정 공유를 손쉽게 만들어 사용자에게 최적의 경험을 제공합니다.

<br>

## 🚀 프로젝트 목표

- Web Architecture 기반의 체계적 웹 프로젝트 설계 및 구현

- Spring Boot와 MyBatis를 이용한 효율적이고 안정적인 서버 구축

- Vue.js 및 Pinia를 활용한 직관적인 사용자 경험 제공

- Kakao Mobility API 활용한 최적의 경로 탐색

- Rabin-Karp 알고리즘을 이용한 빠른 키워드 검색

- 공공 데이터 API 활용 (관광지, 날씨, 전기차 충전소, 일출일몰 정보)

- OAuth 및 REST API 적용한 MSA 구조 경험

<br>

## 📚 기술 스택

### 🖥️ Frontend

- Framework: Vue.js 3

- Routing: Vue Router

- State Management: Pinia

- Styling: Bootstrap, Tailwind CSS

- Build Tool: Vite

### 💽 Backend

- Framework: Spring Boot

- ORM: MyBatis

- Database: MySQL

- Deployment: Apache Tomcat

- Languages & Tools: Java, STS

### 🌐 APIs & Libraries

- 관광지 데이터: 한국관광공사 API

- 지도 및 경로 탐색: Kakao Mobility API

<br>

## 🗂 파일 구조
### 🖥️ Client
```yaml
kiriya-client/
├── index.html                    # Vite 진입점 HTML
├── jsconfig.json                # VSCode 경로 alias 설정
├── package.json                 # 프로젝트 의존성 및 스크립트 정의
├── package-lock.json           # 고정된 의존성 버전 기록
├── .eslintrc-auto-import.json   # 자동 import 관련 ESLint 설정
├── public/                      # 정적 파일 (favicon, 이미지 등)
│   └── favicon.ico              # 브라우저 탭 아이콘
└── src/                         # 주요 소스코드 디렉터리
    ├── main.js                  # 애플리케이션 진입점 (Vue 초기화)
    ├── App.vue                  # 최상위 Vue 컴포넌트
    ├── assets/                  # 이미지, 스타일 등 정적 자산
    ├── components/              # 공통 UI 컴포넌트 모음
    ├── layouts/                 # 전체 페이지에 사용되는 레이아웃 템플릿
    ├── pages/                   # 실제 페이지 단위 뷰 파일
    ├── router/                  # Vue Router 설정 (라우팅 정의)
    ├── store/                   # Pinia 상태 관리 파일
    └── views/                   # 페이지의 구성 요소로 사용되는 뷰

```

### 💽 Server

```yaml
kiriya/
├── src/
│   ├── main/
│   │   ├── java/com/ssafy/
│   │   │   ├── config/      # 스프링 설정 (MapperScan 등)
│   │   │   ├── controller/  # 서버의 요청과 응답을 처리하는 서비스
│   │   │   ├── service/     # 비즈니스 로직을 처리하는 서비스
│   │   │   ├── dao/         # 데이터베이스와의 인터페이스를 담당하는 DAO
│   │   │   ├── dto/         # 데이터 모델을 정의한 DTO 클래스
│   │   │   ├── util/        # 공통 유틸리티
│   │   ├── resources/
│   │   │   ├── mappers      # Mybatis Mapper
│   │   │   ├── scripts      # DB 스크림트
```

<br>


## 🛠️ 기능 명세

### 👤 회원
    - 회원 가입, 로그인, 로그아웃
    - 회원 정보 수정 및 탈퇴
        - 프로필 이미지 등록
    - 비밀번호 찾기
        - 등록된 이메일을 통해 임시 비밀번호 발급

### 🗺️ 관광지
    - 시도, 군구, 관광 타입 별로 필터링하여 관광지 목록 조회
    - 관광지 상세 정보 조회
    - 관광지 좋아요 (버킷리스트 담기) 및 좋아요 취소

### 🛣️ 여행 경로
    - 경로에 관광지 추가, 삭제, 순서 변경
    - 경로에 추가한 관광지 좌표를 바탕으로, 최단 경로 제안 (실제 자동차 경로 기준)
        - Kakao Mobility API의 길찾기 사용으로 변경
        - Held-Karp 알고리즘
    - 경로 내 각 관광지 간의 소요 시간 (자동차 기준) 및 네이버맵 길찾기 링크 제공

### 💗 피드
    - 등록, 조회, 수정, 삭제
        - 작성자 혹은 관리자만 수정, 삭제 가능
        - 피드 이미지 추가
    - 태그 기반으로 필터링 하여 조회
    - 다중 키워드 검색
        - Rabin-Karp 알고리즘 및 Priority Queue 사용
    - 피드 좋아요 및 취소
    - 좋아요 누른 피드 모아보기
    - 내가 작성한 피드 모아보기
    - 피드 댓글 등록, 수정, 삭제

### 📢 공지사항
    - 등록, 조회(페이징), 수정, 삭제
        - 관리자만 작성 가능
    - 다중 키워드 검색
        - Rabin-Karp 알고리즘 및 Priority Queue 사용

### ☎️ QnA
    - 질문 등록, 조회(페이징), 수정, 삭제
    - 제목 기반 질문 검색
    - 답변 등록, 조회, 수정, 삭제
        - 관리자만 작성 가능

### 🧳 여행
    - 여행 생성, 조회, 삭제
    - 여행 이미지 추가

### 🧭 여행 일정
    - 여행에 일정 저장 및 조회
        - 관광 지도에서 경로 불러오기를 통해 확인 가능
    - 일정 수정 (제목, 기간, 경로)
        - 관광지 순서 조정 및 관광지 추가 및 삭제
    - 일정 내 각 관광지 간의 소요 시간 (자동차 기준) 및 네이버맵 길찾기 링크 제공

### 👥 그룹
    - 그룹 생성, 수정 (그룹원 등록, 삭제 포함)
    - 그룹의 여행 등록, 조회, 삭제

<br>

## 📷 DEMO

<table>
  <thead>
    <tr>
      <th>기능 시연 내용</th>
      <th>데모 영상</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>🔑 로그인, 회원가입, 비밀번호 찾기, 회원 정보 수정</td>
      <td>
        <video src="./demos/로그인_회원가입_비밀번호찾기_프로필수정.mp4?raw=true" width="480" controls></video>
      </td>
    </tr>
    <tr>
      <td>🧭 경로 저장, 수정, 조회</td>
      <td>
        <video src="./demos/경로저장_수정_조회.mp4?raw=true" width="480" controls></video>
      </td>
    </tr>
    <tr>
      <td>📅 여행 일정 삭제 및 조회</td>
      <td>
        <video src="./demos/일정삭제_여행조회.mp4?raw=true" width="480" controls></video>
      </td>
    </tr>
    <tr>
      <td>👥 여행 수정 및 그룹 등록</td>
      <td>
        <video src="./demos/여행수정_그룹등록.mp4?raw=true" width="480" controls></video>
      </td>
    </tr>
    <tr>
      <td>🤝 그룹에 여행 공유</td>
      <td>
        <video src="./demos/그룹에_여행공유.mp4?raw=true" width="480" controls></video>
      </td>
    </tr>
    <tr>
      <td>📰 피드 서핑</td>
      <td>
        <video src="./demos/피드.mp4?raw=true" width="480" controls></video>
      </td>
    </tr>
    <tr>
      <td>💬 고객센터&nbsp;(공지사항&nbsp;&amp;&nbsp;QnA)</td>
      <td>
        <video src="./demos/고객센터.mp4" width="480" controls></video>
      </td>
    </tr>
  </tbody>
</table>
