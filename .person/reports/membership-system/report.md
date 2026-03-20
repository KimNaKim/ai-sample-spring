# 🚩 작업 보고서: 회원 탈퇴 기능 구현 (Membership Withdrawal)

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **요구사항 분석**: Phase 2의 미완료 과제인 '회원 탈퇴' 기능을 설계. 보안을 위해 비밀번호 재확인 절차를 포함하기로 결정.
2. **DTO 설계**: `UserRequest.Withdraw`를 추가하여 클라이언트로부터 비밀번호를 안전하게 전달받을 구조 구축.
3. **비즈니스 로직 구현**: `UserService`에서 `PasswordEncoder.matches()`를 사용해 본인 여부를 검증하고, JPA의 `deleteById`를 통해 물리적 삭제(Hard Delete) 수행.
4. **컨트롤러 연동**: 탈퇴 폼 이동(`GET`)과 탈퇴 처리(`POST`) 엔드포인트를 구현하고, 탈퇴 성공 시 세션 무효화(`session.invalidate()`) 처리.
5. **UI 구현**: `withdraw-form.mustache`를 생성하여 사용자에게 데이터 삭제에 대한 강력한 경고 메시지를 전달하고, 디자인 토큰을 활용해 일관된 UI 제공.
6. **진입점 확보**: `update-form.mustache` 하단에 탈퇴 페이지로 이동할 수 있는 링크를 추가.

## 2. 🧩 핵심 코드 (Core Logic)
### UserService.java
```java
@Transactional
public void withdraw(Integer id, UserRequest.Withdraw requestDTO) {
    // 1. 회원 존재 여부 확인
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

    // 2. 비밀번호 일치 여부 검증 (BCrypt matches 사용)
    if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
        throw new RuntimeException("비밀번호가 틀렸습니다.");
    }

    // 3. 물리적 삭제 수행
    userRepository.deleteById(id);
}
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **'헬스장 회원권 해지'**와 같습니다. 단순히 나간다고 말하는 게 아니라, 데스크에 가서 **본인 확인(비밀번호)**을 하고, 그동안의 **기록(데이터)**이 모두 사라진다는 안내를 받은 뒤, 마지막으로 **회원 카드(세션)**를 반납하는 과정과 똑같아요!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **Hard Delete (물리적 삭제)**: `DELETE` 쿼리를 직접 실행하여 DB에서 데이터를 완전히 삭제하는 방식입니다. 데이터 복구가 불가능하므로 반드시 사용자에게 충분한 고지가 필요합니다.
- **Session Invalidation**: `session.invalidate()`는 현재 사용자의 세션을 즉시 만료시켜, 탈퇴 후 더 이상 인증된 상태로 시스템에 머물 수 없게 만듭니다.
- **Security Validation**: `PasswordEncoder.matches(rawPassword, encodedPassword)`를 사용하여 해싱된 비밀번호와 입력된 비밀번호를 안전하게 대조했습니다. 직접적인 비교가 불가능한 단방향 암호화의 특성을 활용한 보안 기법입니다.
