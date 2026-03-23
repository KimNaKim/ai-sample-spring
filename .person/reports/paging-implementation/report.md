# 🚩 작업 보고서: 페이징 기능 및 사용자 유효성 검사 구현

- **작업 일시**: 2026-03-23
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **공통 응답 유틸리티 확장**: `Resp.java`에 자바스크립트 `alert`와 `history.back()`을 결합한 `script()` 메서드를 추가하여 비정상 접근에 대한 즉각적인 피드백 구조를 마련했습니다.
2. **DTO 구조 정밀 설계**: `BoardResponse.java` 내 `Paging` DTO에 현재 페이지(`current`), 첫 페이지 여부(`first`), 마지막 페이지 여부(`last`) 등을 추가하고, Mustache 렌더링을 위해 페이지 번호 객체 리스트(`PageNumber`)를 생성하는 로직을 구현했습니다.
3. **컨트롤러 유효성 검사 강화**: `BoardController`에서 사용자의 `1-based` 요청 번호를 검증(1보다 작거나 마지막 페이지를 초과하는 경우)하여 잘못된 접근 시 브라우저 팝업을 띄우고 이전 페이지로 되돌리는 보안 로직을 적용했습니다.
4. **UI 레이아웃 최적화**: `list.mustache`에 디자인 시스템 토큰을 적용하여 현재 페이지는 강조하고, 이동 불가능한 버튼(이전/다음)은 비활성화(`disabled`) 및 반투명 처리하여 직관적인 UX를 완성했습니다.

## 2. 🧩 핵심 코드 (Core Logic)

### 2.1 컨트롤러 유효성 검사 및 변환
```java
@GetMapping({"/", "/board"})
public String home(@RequestParam(name = "page", defaultValue = "1") int page, Model model, HttpServletResponse response) throws IOException {
    // 1-based 최소값 검증
    if (page < 1) {
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println(Resp.script("1페이지가 시작입니다."));
        return null; // 실행 중단
    }
    
    // 전체 페이지 수 계산 및 최대값 검증
    long totalCount = boardRepository.count();
    int totalPages = (int) Math.ceil((double) totalCount / limit);
    if (totalCount > 0 && page > totalPages) {
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println(Resp.script("마지막 페이지입니다."));
        return null;
    }

    // 0-based 변환 후 서비스 호출 (1 -> 0)
    List<BoardResponse.Max> models = boardService.findAllManual(page - 1);
    // ...
}
```

### 2.2 공통 스크립트 응답 유틸리티
```java
public static String script(String msg) {
    return """
           <script>
               alert("%s");
               history.back();
           </script>
           """.formatted(msg);
}
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **'엘리베이터의 층수 버튼'**을 설계한 것과 같습니다!
> 
> 아파트 지하가 0층(0-based)이라고 해도 주민들은 1층(1-based)이라고 불러야 편하죠. 그래서 버튼 숫자는 1부터 보이게 했습니다. 하지만 누군가 존재하지 않는 100층 버튼을 억지로 누르려 하거나 주소창에 직접 입력하면, 엘리베이터가 **'삐- 잘못된 층입니다'**라는 안내 방송(`alert`)을 내보내고 원래 있던 층(`history.back`)으로 돌려보내도록 안전장치를 만든 셈입니다."

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **1-based vs 0-based 변환**: 데이터베이스의 `OFFSET`은 건너뛸 개수를 의미하므로 0부터 시작하는 것이 수학적으로 유리합니다. 하지만 사용자는 1부터 세는 것이 익숙하므로, 컨트롤러가 이 사이에서 `page - 1` 연산을 수행하는 '번역가' 역할을 합니다.
- **Direct Script Response**: `@Controller`에서 `HttpServletResponse`를 직접 사용하여 HTML을 응답하면, 스프링의 뷰 리졸버를 거치지 않고 브라우저에 즉각적인 명령(JavaScript)을 전달할 수 있습니다. 이는 SSR 환경에서 매우 빠르고 확실한 피드백 수단이 됩니다.
- **Mustache Boolean Logic**: `{{#current}}...{{/current}}` 처럼 객체 리스트의 불리언 필드를 활용하여 현재 페이지에만 특정 CSS 클래스나 스타일을 동적으로 입힐 수 있습니다.
