# 기능 상세 명세 (Specification)

본 문서는 프로젝트 구현을 위한 구체적인 API 및 SSR 엔드포인트와 DTO 구조를 정의합니다. 모든 REST API는 `Resp<T>`를 응답 형식으로 사용하며, 도메인별 플랫 구조를 준수합니다.

---

## 1. 회원 관리 도메인 (User)

### 1.1 SSR 페이지 (UserController)
| 엔드포인트 | HTTP | 템플릿 경로 | 설명 |
| :--- | :--- | :--- | :--- |
| `/join-form` | GET | `user/join-form` | 회원가입 페이지 |
| `/login-form` | GET | `user/login-form` | 로그인 페이지 |
| `/user/update-form` | GET | `user/update-form` | 회원 정보 수정 페이지 (주소 등) |

### 1.2 REST API (UserApiController)
| 엔드포인트 | HTTP | DTO (Request) | 설명 |
| :--- | :--- | :--- | :--- |
| `/api/join` | POST | `UserRequest.Join` | 회원가입 처리 |
| `/api/login` | POST | `UserRequest.Login` | 로그인 처리 (`HttpSession` 저장) |
| `/api/logout` | GET | - | 로그아웃 처리 (`session.invalidate`) |
| `/api/user/username-check` | GET | - | 아이디 중복 체크 (Ajax용) |

### 1.3 DTO 정의
- **Request**: `Join`, `Login`, `Update`
- **Response**: `Min` (id, username, email 등 최소 정보)

---

## 2. 게시글 도메인 (Board)

### 2.1 SSR 페이지 (BoardController)
| 엔드포인트 | HTTP | 템플릿 경로 | 설명 |
| :--- | :--- | :--- | :--- |
| `/` | GET | `board/list` | 메인 페이지 (게시글 목록, 페이징) |
| `/board/{id}` | GET | `board/detail` | 게시글 상세 페이지 (댓글 목록 포함) |
| `/board/save-form` | GET | `board/save-form` | 글쓰기 페이지 (로그인 필수) |
| `/board/{id}/update-form` | GET | `board/update-form` | 글 수정 페이지 (본인 확인 필수) |

### 2.2 REST API (BoardApiController)
| 엔드포인트 | HTTP | DTO (Request) | 설명 |
| :--- | :--- | :--- | :--- |
| `/api/board` | POST | `BoardRequest.Save` | 게시글 작성 |
| `/api/board/{id}` | PUT | `BoardRequest.Update` | 게시글 수정 |
| `/api/board/{id}` | DELETE | - | 게시글 삭제 |

### 2.3 검색 및 필터링
- **목록 조회**: `/board?page=0&keyword=abc` (Query String)
- **검색 로직**: 제목(`title`) 또는 본문(`content`)에 키워드 포함 시 필터링.

### 2.4 DTO 정의
- **Request**: `Save`, `Update`
- **Response**: 
  - `Max`: 목록 조회용 (게시글 기본 정보 + 작성자 username)
  - `Detail`: 상세 조회용 (게시글 정보 + 작성자 정보 + 댓글 리스트)

---

## 3. 댓글 도메인 (Reply)

### 3.1 REST API (ReplyApiController)
| 엔드포인트 | HTTP | DTO (Request) | 설명 |
| :--- | :--- | :--- | :--- |
| `/api/reply` | POST | `ReplyRequest.Save` | 댓글 작성 (Fetch API 기반) |
| `/api/reply/{id}` | DELETE | - | 댓글 삭제 (Fetch API 기반) |

### 3.2 DTO 정의
- **Request**: `Save` (boardId, comment 포함)
- **Response**: `Max` (id, comment, username, createdAt 포함)

---

## 4. 공통 비즈니스 로직 및 제약

### 4.1 권한 및 인증
- **로그인 체크**: `Board` 작성/수정/삭제, `Reply` 작성/삭제 시 세션 확인.
- **본인 확인**: 수정/삭제 요청 시 세션 유저와 데이터 작성 유저 일치 여부 검증.

### 4.2 회원 탈퇴 처리 (Soft Delete)
- 사용자가 탈퇴하더라도 데이터베이스에서 즉시 삭제하지 않고, `is_active` 또는 `status` 필드를 두어 관리하거나 목록 조회 쿼리에서 `JOIN user_tb u ON ... WHERE u.is_active = true` 방식으로 비노출 처리합니다. (기획에 따라 최종 결정)

### 4.3 페이징 및 정렬
- 기본 정렬: `id` 또는 `createdAt` 내림차순 (최신순).
- 페이지 크기: 기본 10건 (`default_batch_fetch_size=10` 고려).
