# 🚩 작업 보고서: AOP를 활용한 전역 유효성 검사 구현

- **작업 일시**: 2026-03-18
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **문제 인식**: 컨트롤러의 모든 메서드에서 `BindingResult.hasErrors()`를 체크하는 중복 코드가 발생할 가능성을 확인했습니다.
2. **AOP 설계**: 핵심 로직(회원가입, 로그인 등)과 공통 관심사(유효성 검사)를 분리하기 위해 Aspect를 활용하기로 결정했습니다.
3. **Aspect 구현**: `ValidAdvice` 클래스를 생성하여 `@Around` 어드바이스로 컨트롤러 메서드 실행 전 파라미터를 가로채도록 구현했습니다.
4. **응답 분기**: 요청이 `@RestController`인지 일반 `@Controller`인지 판단하여 JSON(`Resp.fail`) 또는 자바스크립트(`alert` + `history.back`) 응답을 동적으로 반환하도록 설정했습니다.
5. **적용**: `UserController.join` 메서드에 `@Valid`를 적용하여 수동 검증 코드 없이도 유효성 검사가 작동함을 확인했습니다.

## 2. 🧩 핵심 코드 (Core Logic)

### ValidAdvice.java (Aspect)
```java
@Around("execution(* com.example.demo..*Controller.*(..))")
public Object validationBind(ProceedingJoinPoint jp) throws Throwable {
    Object[] args = jp.getArgs();
    for (Object arg : args) {
        if (arg instanceof BindingResult) {
            BindingResult br = (BindingResult) arg;
            if (br.hasErrors()) {
                String errMsg = br.getFieldErrors().get(0).getDefaultMessage();
                
                // REST와 SSR 응답을 구분하여 처리
                if (jp.getTarget().getClass().isAnnotationPresent(RestController.class)) {
                    return Resp.fail(HttpStatus.BAD_REQUEST, errMsg);
                } else {
                    return script(errMsg); // <script>alert(...); history.back();</script>
                }
            }
        }
    }
    return jp.proceed();
}
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **'클럽 입구의 보안 요원'**을 배치한 것과 같습니다.
> 
> 이전에는 모든 직원이 손님을 맞이할 때마다 일일이 신분증 검사(Validation)를 해야 했다면, 이제는 입구에 '공통 보안 요원(AOP)'을 세워둔 것입니다. 보안 요원은 손님이 규정에 맞지 않으면 바로 돌려보내고, 규정을 통과한 손님만 직원에게 안내합니다. 덕분에 직원(Controller)은 오로지 서비스(Business Logic)에만 집중할 수 있게 되었습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)

### 1) AOP (Aspect Oriented Programming)
- **개념**: 관점 지향 프로그래밍으로, 코드의 핵심 로직과 공통적으로 나타나는 부가 기능을 분리하여 모듈화하는 방식입니다.
- **핵심 요소**:
    - **Pointcut**: 부가 기능이 적용될 위치(메서드 패턴)
    - **Advice**: 언제(실행 전, 후, 주변) 부가 기능을 실행할지 정의
    - **JoinPoint**: Advice가 적용될 수 있는 지점 (파라미터 정보 등 포함)

### 2) ProceedingJoinPoint
- `jp.proceed()`를 호출하면 가로챘던 원래의 메서드를 실행합니다. 만약 유효성 검사 실패 시 이 메서드를 호출하지 않고 에러 응답을 리턴함으로써, 실제 비즈니스 로직(Service 호출 등)이 실행되는 것을 방지합니다.
