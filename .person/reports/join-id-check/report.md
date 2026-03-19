# 🚩 작업 보고서: 아이디 중복 체크 (AJAX) 구현

- **작업 일시**: 2026-03-18
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **문제 진단**: `UserService`에서 중복 아이디 발견 시 `RuntimeException`을 던지고 있었으나, 이를 JSON 응답(`Resp.fail`)으로 변환해줄 전역 예외 처리기가 부재함을 확인했습니다.
2. **백엔드 보완**: `GlobalExceptionHandler`를 생성하여 `RuntimeException` 발생 시 HTTP 400 상태 코드와 함께 공통 응답 객체(`Resp`)를 반환하도록 설정했습니다.
3. **프론트엔드 강화**: `join-form.mustache`에 Fetch API를 이용한 비동기 통신 로직을 구현했습니다.
4. **UI/UX 개선**: 디자인 시스템(CSS 변수)을 활용하여 성공/실패 메시지 색상을 구분하고, 아이디 변경 시 중복 체크 상태를 자동으로 리셋하는 편의 기능을 추가했습니다.
5. **검증**: 아이디 길이에 따른 유효성 검사(4~20자)를 프론트엔드에 추가하여 불필요한 서버 요청을 방지했습니다.

## 2. 🧩 핵심 코드 (Core Logic)

### GlobalExceptionHandler.java
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 모든 RuntimeException을 가로채서 에러 메시지를 JSON으로 반환
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
```

### join-form.mustache (JS Logic)
```javascript
function usernameSameCheck() {
    let username = document.querySelector("#username").value;
    // 1. 유효성 검사 (4~20자)
    if (!username || username.length < 4 || username.length > 20) {
        alert("아이디는 4~20자 사이여야 합니다.");
        return;
    }
    // 2. 비동기 요청 (Fetch)
    fetch(`/api/user/same-check?username=${username}`)
        .then(response => response.json())
        .then(responseBody => {
            if (responseBody.status === 200) {
                isSameCheck = true; // 중복 체크 성공 상태 저장
                // ... UI 업데이트 (초록색 메시지, 버튼 활성화)
            } else {
                isSameCheck = false;
                // ... UI 업데이트 (빨간색 메시지, 버튼 비활성화)
            }
        });
}
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **'도서관 회원증 발급'**과 같습니다.
> 
> 이전에는 새로운 회원이 이미 사용 중인 이름을 가져오면 직원이 당황해서 말을 못 하는 상황(에러 페이지)이었다면, 이제는 직원이 친절하게 **'죄송하지만 이미 사용 중인 이름이에요'**라고 안내판을 보여주는 것(GlobalExceptionHandler)과 같습니다. 또한, 회원이 이름을 쓰는 도중에는 실시간으로 **'이 이름은 쓸 수 있어요!'**라는 표시등(Fetch API)이 켜지게 되어 훨씬 편리해진 셈이죠!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

### 1) Global Exception Handling (@RestControllerAdvice)
- **개념**: 애플리케이션 전역에서 발생하는 예외를 한곳에서 관리하는 기법입니다.
- **장점**: 각 컨트롤러마다 `try-catch`를 작성할 필요가 없어 코드 중복이 줄어들고, 클라이언트에게 일관된 응답 형식(예: `Resp` 객체)을 보장할 수 있습니다.

### 2) Fetch API & 비동기 통신 (AJAX)
- **개념**: 페이지 전체를 새로고침하지 않고, 브라우저 백그라운드에서 서버와 데이터를 주고받는 방식입니다.
- **흐름**: 
  1. 사용자 클릭 
  2. 브라우저가 서버에 JSON 데이터 요청 
  3. 서버 응답 
  4. JavaScript가 DOM을 조작하여 화면 일부만 갱신
- **효과**: 사용자 경험(UX)이 매끄러워지며 서버 부하를 줄일 수 있습니다.
