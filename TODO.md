# 오늘의 할 일 (TODO)

## Phase 2: 회원 인증 시스템 (Membership System) - [완료]

- [x] **T-2.1 회원가입 기능 및 화면 구현**
  - [x] BCrypt 해시 저장 및 주소 API 연동
- [x] **T-2.2 로그인 기능 및 세션 관리**
  - [x] 하이브리드 검증 로직 및 세션 메뉴 분기
- [x] **T-2.3 회원 정보 수정 (Update)**
  - [x] 기존 정보 바인딩 및 세션 자동 갱신

## Phase 3: 게시글 관리 (Board Domain) - [대기]

- [ ] **T-3.1 게시글 목록 보기 (List)**
  - [ ] **[DTO]** `BoardResponse.Max` 목록용 DTO 작성 (작성자 username 포함)
  - [ ] **[Service]** `BoardService.findAll` 구현 (페이징 `Pageable` 적용)
  - [ ] **[SSR]** `BoardController` GET `/` (또는 `/board/list`) 구현
  - [ ] **[UI]** `board/list.mustache` 작성 (Mustache 반복문 활용)
  - [ ] **[Validation]** 초기 데이터(`data.sql`)가 메인 페이지에 정상 출력되는지 확인

- [ ] **T-3.2 게시글 상세 보기 (Detail)**
  - [ ] **[DTO]** `BoardResponse.Detail` 작성 (게시글 + 작성자 + 댓글 리스트)
  - [ ] **[Service]** `BoardService.findById` 구현 (LAZY 페칭 최적화)
  - [ ] **[SSR]** `BoardController` GET `/board/{id}` 구현
  - [ ] **[UI]** `board/detail.mustache` 작성 (수정/삭제 버튼 권한 분기)
