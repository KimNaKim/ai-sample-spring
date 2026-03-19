# 기술 스택 및 아키텍처 (Technical Stack & Architecture)

본 프로젝트는 유지보수성과 확장성을 고려하여 **도메인 기반 플랫 구조**와 **무상태(Stateless) 지향적 서버 구성**을 채택합니다.

---

## 1. 기술 스택 (Tech Stack)

### 1.1 Backend
- **Language**: Java 21
- **Framework**: Spring Boot 4.0.3
- **Security**: Spring Security (PasswordEncoder 기반 인증)
- **ORM**: Spring Data JPA (Hibernate)
- **Validation**: Jakarta Bean Validation (Hibernate Validator)
- **Lombok**: 보일러플레이트 코드 제거 및 가독성 향상

### 1.2 Database
- **Primary DB**: H2 (In-memory, 개발 환경)
- **Connection Pool**: HikariCP (Default)
- **Naming Strategy**: Snake Case (`_tb` 접미사 필수)

### 1.3 Frontend
- **Template Engine**: Mustache (Server-Side Rendering)
- **Styling**: Vanilla CSS (커스텀 디자인 시스템 적용)
- **JavaScript**: Vanilla JS + Fetch API (Ajax 비동기 통신용)

---

## 2. 아키텍처 원칙 (Architectural Principles)

### 2.1 도메인 기반 플랫 구조 (Domain-based Flat Structure)
- 레이어별 폴더링 대신 **도메인별 폴더링**을 수행합니다.
- 한 도메인 폴더 내에 `Entity`, `Service`, `Repository`, `Controller`, `DTO`가 모두 위치합니다.
- 예외: 도메인과 무관한 유틸리티는 `_core/` 폴더에 위치합니다.

### 2.2 SSR & REST 분리
- `@Controller` (화면 전환)와 `@RestController` (데이터 통신)는 **파일을 엄격히 분리**합니다.
- REST API는 항상 `/api` 접두사를 사용합니다.

### 2.3 보안 및 세션 관리
- **Authentication**: Spring Security의 `PasswordEncoder`를 활용하여 비밀번호를 암호화하고, `matches()` 메서드로 검증합니다.
- **Session Management**: 인증 성공 후 `HttpSession`을 통해 세션 유저 정보를 관리하며, 로그아웃은 Spring Security 필터를 통해 수행합니다.
- **Authorization**: `SecurityConfig`를 통해 접근 권한 및 로그아웃 경로(`/logout`)를 관리합니다.

---

## 3. 주요 설정 및 제약 (Key Configurations)

### 3.1 JPA 최적화
- **OSIV (Open Session In View)**: `false` (강제). 컨트롤러에서 지연 로딩을 방지하기 위해 Service 레이어에서 DTO 변환을 완료합니다.
- **Fetch Strategy**: 모든 연관관계는 `FetchType.LAZY`를 사용합니다.
- **Batch Size**: `spring.jpa.properties.hibernate.default_batch_fetch_size=10` 설정을 통해 1:N 관계의 N+1 문제를 최적화합니다.

### 3.2 공통 응답 및 예외 처리
- **Response Wrapper**: 모든 REST 응답은 `Resp<T>` 객체로 래핑하여 `status`, `msg`, `body` 구조를 유지합니다.
- **Global Exception Handling**: `@ControllerAdvice`를 통해 공통 예외(400, 403, 404, 500)를 일관되게 처리합니다.

---

## 4. 데이터 정합성 (Data Integrity)
- **PK Strategy**: `Integer` 타입 및 `GenerationType.IDENTITY` (AUTO_INCREMENT).
- **Audit**: `@CreationTimestamp`를 활용하여 생성 일시를 자동 기록합니다.
- **Soft Delete**: 회원 탈퇴 등의 민감한 정보는 즉시 물리 삭제하지 않고 논리 삭제(Soft Delete)를 고려합니다.
