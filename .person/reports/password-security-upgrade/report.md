# 🚩 작업 보고서: 비밀번호 보안 강화 및 Spring Security 통합

- **작업 일시**: 2026-03-19
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **환경 최적화**: Spring Boot 버전을 최신 안정화 버전인 `4.0.3`으로 업데이트하고, 관련 의존성(`spring-boot-starter-aspectj`)을 조정했습니다.
2. **보안 프레임워크 도입**: 기존의 단순 `jbcrypt` 라이브러리를 제거하고, 산업 표준인 `spring-boot-starter-security`를 도입했습니다.
3. **보안 설정 구성**: `SecurityConfig`를 통해 `BCryptPasswordEncoder`를 Bean으로 등록하고, 현재의 수동 로그인 방식을 유지하도록 보안 필터를 설정했습니다.
4. **서비스 로직 리팩토링**: `UserService`에서 수동으로 관리하던 BCrypt 로직을 삭제하고, `PasswordEncoder.matches()`를 사용한 표준 검증 방식으로 교체했습니다.
5. **데이터 동기화**: `data.sql`의 평문 더미 데이터를 실제 BCrypt 해시값으로 업데이트하여 보안 일관성을 확보했습니다.
6. **테스트 검증**: `UserServiceTest`를 리팩토링하여 새로운 보안 구조에서도 회원가입 및 로그인이 정상 작동함을 확인했습니다.

## 2. 🧩 핵심 코드 (Core Logic)
```java
// UserService.java - PasswordEncoder를 활용한 표준 로그인 검증
@Transactional
public UserResponse.Min login(UserRequest.Login requestDTO) {
    // 1. 유저 존재 여부 확인
    User user = userRepository.findByUsername(requestDTO.getUsername())
            .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 틀렸습니다."));

    // 2. PasswordEncoder.matches()를 사용한 해시 비교 (평문 대조 로직 완전 제거)
    if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
        throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
    }

    return new UserResponse.Min(user);
}
```

## 3. 🍦 상세비유: 금고 열쇠 시스템 업그레이드 (Easy Analogy)
> "이번 작업은 **수동 자물쇠 시스템을 최신식 생체 인식 시스템으로 교체**한 것과 같습니다. 이전에는 열쇠(비밀번호)가 진짜인지 주머니에서 꺼내 직접 대조해보는 과정(평문 비교)이 있었지만, 이제는 입력된 정보를 시스템이 암호화된 데이터베이스와 정교하게 매칭(`matches`)해보고 승인 여부만 알려주는 훨씬 안전한 방식으로 바뀌었습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **Spring Security PasswordEncoder**: 비밀번호 저장을 위한 단방향 해시 암호화를 지원하는 인터페이스입니다. 구현체인 `BCryptPasswordEncoder`는 무작위 솔트(Salt)를 사용하여 동일한 비밀번호라도 매번 다른 해시값을 생성함으로써 레인보우 테이블 공격을 방어합니다.
- **matches() 메서드**: 평문 비밀번호와 저장된 해시값을 비교하는 메서드입니다. 내부적으로 저장된 해시값에서 솔트를 추출하여 평문을 다시 해싱한 뒤 비교하므로, 개발자가 직접 솔트를 관리하거나 평문 비교 로직을 짤 필요가 없어 실수를 방지합니다.
- **Dependency Management**: `jbcrypt` 라이브러리를 직접 의존하는 대신 `spring-boot-starter-security`를 통해 관리함으로써, 스프링 생태계의 다른 보안 기능들과의 호환성을 높였습니다.
