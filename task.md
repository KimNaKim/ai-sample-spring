# 프로젝트 구현 상세 태스크 (Granular Tasks)

본 문서는 `phase.md`를 바탕으로 한 상세 체크리스트입니다. 각 기능은 [DTO -> Service -> Controller -> UI -> Validation] 단계를 거치며, 완료 후 `Validation` 항목을 반드시 체크합니다.

---

## Phase 1: 기반 설정 및 공통 검증 (Foundation) - [완료]
- [x] **1.1 JPA 및 성능 설정**
  - [x] `application.properties`: `OSIV=false`, `batch_fetch_size=10` 설정
  - [x] JPA 로깅 및 H2-Console 활성화
- [x] **1.2 전역 예외 처리 (Global Error Handling)**
  - [x] `Resp.java` (공통 응답 래퍼) 구현
  - [x] `GlobalExceptionHandler.java`: `@ControllerAdvice` 기반 REST 에러 응답
  - [x] SSR 전용 에러 페이지 (`error/404.mustache`, `500.mustache`) 매핑
- [x] **1.3 공통 레이아웃 (Mustache Layout)**
  - [x] `layout/header.mustache`, `layout/footer.mustache` 작성
  - [x] `static/css/style.css` 기본 스타일 정의

---

## Phase 2: 회원 관리 (User Domain) - [완료]

### 2.1 회원가입 (Join)
- [x] **[DTO]** `UserRequest.Join` 클래스 작성 (username, password, email, 주소 필드 추가)
- [x] **[Service]** `UserService.save` 구현 (BCrypt 암호화 적용)
- [x] **[SSR]** `UserController` GET `/join-form` 메서드 구현
- [x] **[UI]** `user/join-form.mustache` 작성 (디자인 시스템 & 주소 API 적용)
- [x] **[Validation]** 회원가입 후 로그인 페이지 이동 및 DB(`user_tb`) 적재 확인

### 2.2 로그인 (Login)
- [x] **[DTO]** `UserRequest.Login` 클래스 작성
- [x] **[Service]** `UserService.login` 구현 (BCrypt 검증 및 평문 마이그레이션 로직 포함)
- [x] **[SSR]** `UserController` GET `/login-form` 메서드 구현
- [x] **[SSR]** `UserController` POST `/login` 메서드 (로그인 처리 및 세션 저장)
- [x] **[UI]** `user/login-form.mustache` 작성
- [x] **[Validation]** 로그인 성공 시 메인 페이지 이동 및 헤더(메뉴 분기) 변화 확인

### 2.3 회원 정보 수정 (Update)
- [x] **[DTO]** `UserRequest.Update` 클래스 작성
- [x] **[Service]** `UserService.update` 구현 (더티 체킹 활용)
- [x] **[SSR]** `UserController` GET `/user/update-form` (세션 확인 및 데이터 바인딩)
- [x] **[UI]** `user/update-form.mustache` 작성 (기존 정보 렌더링 및 주소 API 연동)
- [x] **[Validation]** 수정 후 세션 갱신 및 메인 페이지 반영 확인

### 2.4 회원 전용 API (REST)
- [x] **[REST]** `UserApiController` GET `/api/user/same-check` (아이디 중복 체크)
- [x] **[Validation]** Fetch API를 통한 비동기 응답 확인

---

## Phase 3: 게시글 관리 및 페이징 Deep Dive (Learning Focus) - [진행 중]

### 3.0 환경 구축 (Data Preparation)
- [x] **[Data]** `data.sql`에 테스트용 게시글 20개 이상 삽입 완료

### 3.1 SQL 레벨 페이징 (Database Layer)
- [ ] **[H2]** 콘솔에서 `LIMIT`, `OFFSET` 수동 쿼리 실행 및 결과 비교 (0 vs 3 vs 6)
- [ ] **[Repo]** `BoardRepository`에 `@Query`를 활용한 수동 페이징 메서드 구현 (인자: `limit`, `offset`)
- [ ] **[Log]** Hibernate 실행 쿼리 로그를 통해 실제 물리 쿼리 관찰

### 3.2 메타데이터 계산 (Business Logic)
- [ ] **[DTO]** `PagingDTO` 설계 (현재 페이지, 시작/끝 페이지, 이전/다음 존재 여부)
- [ ] **[Service]** 총 게시글 수(`COUNT`) 조회 및 페이징 메타데이터 수동 계산 로직 구현

### 3.3 UI 동적 렌더링 (View Layer)
- [ ] **[UI]** `board/list.mustache` 하단에 페이징 버튼 레이아웃 작성
- [ ] **[Mustache]** 계산된 메타데이터를 활용하여 버튼 활성/비활성 및 번호 노출 제어

### 3.4 JPA 추상화 연결 (Refactoring)
- [ ] **[JPA]** 수동 로직을 `Pageable`과 `Page<T>` 인터페이스로 전환
- [ ] **[Compare]** 직접 짠 로직과 스프링 제공 기능의 차이점 및 편의성 분석

### 3.5 게시글 상세 보기 (Detail)
- [ ] **[DTO]** `BoardResponse.Detail` 작성 (게시글 + 작성자 + 댓글 리스트)
- [ ] **[Service]** `BoardService.findById` 구현 (LAZY 페칭 최적화)
- [ ] **[SSR]** `BoardController` GET `/board/{id}` 구현
- [ ] **[UI]** `board/detail.mustache` 작성 (수정/삭제 버튼 권한 분기)
