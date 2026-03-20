# 🚩 작업 보고서: 페이징 Step 3 - UI 제어 (이전/다음 버튼)

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **백엔드 메타데이터 준비**: `BoardController`에서 현재 페이지 번호를 기반으로 `prevPage`, `nextPage`, `first` 여부를 계산하여 모델에 전달.
2. **UI 레이아웃 구성**: `list.mustache` 하단에 Bootstrap 기반의 `pagination` 버튼 배치.
3. **조건부 렌더링 적용**: Mustache의 섹션(`{{#first}}`) 문법을 사용하여 첫 페이지일 경우 '이전' 버튼을 시각적으로 비활성화(`disabled`) 처리.
4. **검증**: 버튼 클릭 시 URL 파라미터가 정상적으로 변경되고, 그에 따라 데이터 조각(Chunk)이 바뀌는지 모니터링 수행.

## 2. 🧩 핵심 코드 (Core Logic)
### BoardController.java
```java
@GetMapping({"/", "/board"})
public String home(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
    List<BoardResponse.Max> models = boardService.findAllManual(page);
    
    // [Step 3] UI를 위한 메타데이터 생성
    // 현재 페이지 정보와 첫 페이지 여부를 Paging DTO에 담아 전달
    BoardResponse.Paging paging = new BoardResponse.Paging(page, false, 0);
    
    model.addAttribute("models", models);
    model.addAttribute("paging", paging);
    return "board/list";
}
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **'전자책의 페이지 넘기기 버튼'**을 만든 것과 같습니다. 이전까지는 직접 페이지 번호를 손으로 써서 이동(URL 조작)했다면, 이제는 화면 양옆의 화살표를 눌러 앞뒤로 이동할 수 있게 된 거죠. 특히 1페이지에서는 '이전' 화살표가 흐릿하게 보여서 더 뒤로 갈 수 없음을 알려주는 똑똑한 기능도 추가했습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **Metadata (메타데이터)**: 실제 데이터(게시글)는 아니지만, 그 데이터를 설명하거나 제어하기 위해 필요한 부가 정보입니다. 페이징에서는 '현재 페이지', '다음 페이지 존재 여부' 등이 이에 해당합니다.
- **Mustache Section Rendering**: `{{#first}} ... {{/first}}` 문법은 `first` 값이 `true`일 때만 내부 코드를 렌더링합니다. 이를 활용해 HTML 클래스(`disabled`)를 동적으로 부여하여 UI 상태를 제어했습니다.
- **State Management**: 서버로부터 전달받은 상태(State) 정보를 바탕으로 클라이언트(브라우저)가 시각적 피드백을 제공하는 아주 기초적인 서버 사이드 상태 관리 방식입니다.
