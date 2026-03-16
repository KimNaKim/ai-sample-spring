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

## Phase 2: 회원 관리 (User Domain) - [진행 중]

### 2.1 회원가입 (Join)
- [x] **[DTO]** `UserRequest.Join` 클래스 작성 (username, password, email)
- [x] **[Service]** `UserService.join` 구현 (중복 체크 포함)
- [ ] **[SSR]** `UserController` GET `/join-form` 메서드 구현
- [ ] **[UI]** `user/join-form.mustache` 작성 (Form post, name 속성 매핑)
- [ ] **[Validation]** 회원가입 후 로그인 페이지 이동 및 DB(`user_tb`) 적재 확인

### 2.2 로그인 (Login)
- [x] **[DTO]** `UserRequest.Login` 클래스 작성
- [x] **[Service]** `UserService.login` 구현 (`HttpSession`에 `UserResponse.Min` 저장)
- [ ] **[SSR]** `UserController` GET `/login-form` 메서드 구현
- [ ] **[SSR]** `UserController` POST `/login` 메서드 (로그인 처리 및 리다이렉트)
- [ ] **[UI]** `user/login-form.mustache` 작성
- [ ] **[Validation]** 로그인 성공 시 메인 페이지 이동 및 헤더(로그아웃 버튼) 변화 확인

### 2.3 회원 정보 수정 (Update)
- [x] **[DTO]** `UserRequest.Update` 클래스 작성
- [ ] **[Service]** `UserService.update` 구현 (더티 체킹 활용)
- [ ] **[SSR]** `UserController` GET `/user/update-form` (세션 확인 필수)
- [ ] **[UI]** `user/update-form.mustache` 작성 (기존 정보 렌더링)
- [ ] **[Validation]** 수정 후 상세 정보 또는 메인 페이지에서 반영 확인

### 2.4 회원 전용 API (REST)
- [ ] **[REST]** `UserApiController` POST `/api/join` (필요 시)
- [ ] **[REST]** `UserApiController` GET `/api/username-same-check` (아이디 중복 체크)
- [ ] **[Validation]** Postman 또는 브라우저 콘솔을 통한 JSON 응답(`Resp.ok`) 확인

---

## Phase 3: 게시글 관리 (Board Domain)

### 3.1 게시글 목록 보기 (List)
- [ ] **[DTO]** `BoardResponse.Max` 목록용 DTO 작성 (작성자 username 포함)
- [ ] **[Service]** `BoardService.findAll` 구현 (페이징 `Pageable` 적용)
- [ ] **[SSR]** `BoardController` GET `/` (또는 `/board/list`) 구현
- [ ] **[UI]** `board/list.mustache` 작성 (Mustache 반복문 활용)
- [ ] **[Validation]** 초기 데이터(`data.sql`)가 메인 페이지에 정상 출력되는지 확인

### 3.2 게시글 상세 보기 (Detail)
- [ ] **[DTO]** `BoardResponse.Detail` 작성 (게시글 + 작성자 + 댓글 리스트)
- [ ] **[Service]** `BoardService.findById` 구현 (LAZY 페칭 최적화)
- [ ] **[SSR]** `BoardController` GET `/board/{id}` 구현
- [ ] **[UI]** `board/detail.mustache` 작성 (수정/삭제 버튼 권한 분기)
- [ ] **[Validation]** 존재하지 않는 ID 접근 시 404 에러 페이지 처리 확인

### 3.3 게시글 작성 (Save)
- [ ] **[DTO]** `BoardRequest.Save` 작성
- [ ] **[Service]** `BoardService.save` 구현 (세션 유저와 연관관계 매핑)
- [ ] **[SSR]** `BoardController` GET `/board/save-form`
- [ ] **[SSR]** `BoardController` POST `/board/save`
- [ ] **[UI]** `board/save-form.mustache` 작성
- [ ] **[Validation]** 작성 후 목록 페이지 상단에 신규 게시글 노출 확인

### 3.4 게시글 수정/삭제 (Update/Delete)
- [ ] **[DTO]** `BoardRequest.Update` 작성
- [ ] **[Service]** `BoardService.update`, `deleteById` 구현 (작성자 본인 여부 검증)
- [ ] **[SSR]** `BoardController` POST `/board/{id}/update`, `/board/{id}/delete`
- [ ] **[UI]** `board/update-form.mustache` 작성
- [ ] **[Validation]** 타인의 글 수정/삭제 시도 시 권한 에러(`Resp.fail`) 발생 확인

---

## Phase 4: 댓글 관리 (Reply Domain)

### 4.1 댓글 작성 (Save)
- [ ] **[DTO]** `ReplyRequest.Save` 작성 (boardId, comment)
- [ ] **[Service]** `ReplyService.save` 구현
- [ ] **[REST]** `ReplyApiController` POST `/api/reply` (AJAX 권장)
- [ ] **[UI]** `board/detail.mustache` 하단 댓글 작성 스크립트(fetch) 연동
- [ ] **[Validation]** 댓글 작성 시 새로고침 없이(또는 리다이렉트 후) 목록에 즉시 반영 확인

### 4.2 댓글 삭제 (Delete)
- [ ] **[Service]** `ReplyService.delete` 구현 (작성자 검증)
- [ ] **[REST]** `ReplyApiController` DELETE `/api/reply/{id}`
- [ ] **[Validation]** 본인 댓글만 삭제 버튼이 활성화되는지 UI 및 API 검증

---

## Phase 5: 최종 안정화 및 보안 (Polishing)

### 5.1 인증 인터셉터 (LoginInterceptor)
- [ ] `_core/interceptor/LoginInterceptor.java` 구현
- [ ] `WebMvcConfig`: `/board/**`, `/user/update-form`, `/api/**` 등 경로 보호
- [ ] **Validation**: 비로그인 상태로 글쓰기 접근 시 로그인 페이지 리다이렉트 확인

### 5.2 유효성 검사 및 정교화
- [ ] Server-side Validation: `@Valid` 또는 수동 검증 추가 (공백 체크 등)
- [ ] Client-side Validation: HTML5 `required` 또는 간단한 JS 정규표현식
- [ ] 디자인 디테일 수정: `style.css` 및 `design-system.md` 반영

### 5.3 최종 통합 테스트
- [ ] 전체 사용자 시나리오(회원가입 -> 로그인 -> 게시글 작성 -> 댓글 작성 -> 정보 수정 -> 탈퇴) 수행
