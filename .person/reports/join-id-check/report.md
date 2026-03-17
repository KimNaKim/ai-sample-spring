# 🚩 작업 보고서: 회원가입 아이디 중복 체크 (AJAX)

- **작업 일시**: 2026-03-17
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **요구사항 분석**: `todo.md`의 2.1 Step 1(아이디 중복 체크) 작업 내용 확인.
2. **백엔드 구현**: `UserService`에 중복 확인 로직을 추가하고, `UserApiController`를 통해 외부로 노출.
3. **컨트롤러 매핑**: `UserController`에 회원가입 페이지 요청 경로 추가.
4. **프론트엔드 구현**: Mustache 템플릿에 `fetch` API를 사용한 비동기 통신 로직 작성.
5. **UI 검증**: 중복 체크 성공 시 가입 버튼 활성화 및 메시지 상태 변경 로직 적용.

## 2. 🧩 핵심 코드 (Core Logic)
```java
// UserApiController.java
@GetMapping("/api/user/same-check")
public ResponseEntity<?> sameCheck(@RequestParam("username") String username) {
    userService.sameCheck(username); // 중복 시 Exception 발생
    return Resp.ok("사용 가능한 아이디입니다.");
}

// UserService.java
public void sameCheck(String username) {
    userRepository.findByUsername(username)
            .ifPresent(user -> {
                throw new RuntimeException("이미 존재하는 아이디입니다.");
            });
}
```

```javascript
// join-form.mustache
fetch(`/api/user/same-check?username=${username}`)
    .then(response => response.json())
    .then(responseBody => {
        if (responseBody.status === 200) {
            isSameCheck = true;
            document.querySelector("#btnJoin").disabled = false;
            // ... UI 업데이트
        }
    });
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **도서관 회원 카드를 만들기 전에 이름표가 겹치는지 확인하는 것**과 같습니다. 
> 사서 선생님(Controller)에게 이름을 물어보면, 선생님은 장부(Database)를 확인해서 이미 그 이름이 있는지 알려줍니다. 
> 이름이 없다면(200 OK), 비로소 가입 신청서(Submit)를 제출할 수 있는 연필(버튼 활성화)을 빌려주는 방식입니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **AJAX (Fetch API)**: 페이지 전체를 새로고침하지 않고 브라우저 뒷단에서 서버와 데이터를 주고받는 방식입니다. 사용자 경험(UX)을 크게 향상시킵니다.
- **REST API**: `/api` 접두사를 사용하여 데이터만을 주고받는 순수 데이터 엔드포인트를 설계했습니다.
- **RuntimeException & Global Handling**: 서비스 계층에서 예외를 던지면, 공통 예외 처리기(`GlobalExceptionHandler`)가 이를 낚아채서 `Resp.fail()`로 응답하는 구조를 활용했습니다.
