# ERD (Entity Relationship Diagram)

본 프로젝트는 JPA를 사용하며, `.ai/rules/code-rule.md`에 명시된 규칙(PK: Integer, LAZY 페치, `{domain}_tb` 테이블명)을 준수합니다.

## 1. 테이블 설계 (Schema)

### 1.1 `user_tb` (회원)
| 컬럼명 | 타입 | 제약조건 | 설명 |
| :--- | :--- | :--- | :--- |
| `id` | `Integer` | PK, IDENTITY | 회원 고유 식별자 |
| `username` | `String` | UNIQUE, NOT NULL | 사용자 아이디 |
| `password` | `String` | NOT NULL (100) | 암호화된 비밀번호 |
| `email` | `String` | - | 이메일 주소 |
| `postcode` | `String` | - | 우편번호 |
| `address` | `String` | - | 기본 주소 |
| `detail_address` | `String` | - | 상세 주소 |
| `extra_address` | `String` | - | 참고 항목 (주소) |
| `created_at` | `LocalDateTime` | @CreationTimestamp | 가입 일시 |

### 1.2 `board_tb` (게시글)
| 컬럼명 | 타입 | 제약조건 | 설명 |
| :--- | :--- | :--- | :--- |
| `id` | `Integer` | PK, IDENTITY | 게시글 고유 식별자 |
| `title` | `String` | NOT NULL | 게시글 제목 |
| `content` | `String` | NOT NULL | 게시글 본문 (텍스트) |
| `user_id` | `Integer` | FK (user_tb), LAZY | 작성자 ID |
| `created_at` | `LocalDateTime` | @CreationTimestamp | 작성 일시 |

### 1.3 `reply_tb` (댓글)
| 컬럼명 | 타입 | 제약조건 | 설명 |
| :--- | :--- | :--- | :--- |
| `id` | `Integer` | PK, IDENTITY | 댓글 고유 식별자 |
| `comment` | `String` | NOT NULL | 댓글 내용 |
| `user_id` | `Integer` | FK (user_tb), LAZY | 댓글 작성자 ID |
| `board_id` | `Integer` | FK (board_tb), LAZY | 대상 게시글 ID |
| `created_at` | `LocalDateTime` | @CreationTimestamp | 작성 일시 |

---

## 2. 관계 정의 (Relationships)

### 2.1 User : Board (1 : N)
- 한 명의 회원은 여러 개의 게시글을 작성할 수 있습니다.
- `board_tb`에서 `user_id` 외래키를 가집니다.

### 2.2 Board : Reply (1 : N)
- 하나의 게시글에는 여러 개의 댓글이 달릴 수 있습니다.
- `reply_tb`에서 `board_id` 외래키를 가집니다.

### 2.3 User : Reply (1 : N)
- 한 명의 회원은 여러 개의 댓글을 작성할 수 있습니다.
- `reply_tb`에서 `user_id` 외래키를 가집니다.

---

## 3. 구현 특이사항 (Implementation Details)
- **페치 전략**: 모든 연관관계는 `FetchType.LAZY`로 설정되어 N+1 문제를 방지합니다.
- **PK 전략**: `GenerationType.IDENTITY`를 사용하여 MySQL/H2 등의 AUTO_INCREMENT를 활용합니다.
- **날짜 타입**: `@CreationTimestamp`를 통해 `createdAt`이 자동으로 기록됩니다.
