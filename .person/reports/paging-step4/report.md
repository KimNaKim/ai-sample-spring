# 🚩 작업 보고서: 페이징 Step 4 - 전체 페이지 계산 및 번호 UI

- **작업 일시**: 2026-03-20
- **진행 단계**: 완료

## 1. 🌊 전체 작업 흐름 (Workflow)
1. **전체 데이터 수 파악**: `boardRepository.count()`를 사용하여 데이터베이스에 저장된 게시글의 총 개수를 조회.
2. **수학적 계산 수행**: 
   - `총 페이지 수 = ceil(전체 개수 / 페이지당 개수)` 로직을 통해 마지막 페이지 인덱스를 산출.
   - 현재 페이지와 마지막 페이지를 비교하여 `last` 여부를 확정.
3. **번호 리스트 생성**: `Paging` DTO에서 루프를 통해 `0`부터 `totalPages - 1`까지의 정수 리스트를 생성.
4. **UI 완성**: `list.mustache`에서 숫자 버튼 목록을 출력하고, '다음' 버튼에 최종 비활성화 조건을 적용.

## 2. 🧩 핵심 코드 (Core Logic)
### BoardController.java
```java
long totalCount = boardRepository.count(); // 전체 개수 조회
int totalPages = (int) Math.ceil((double) totalCount / 3); // 올림 계산 (20/3 = 6.66 -> 7)
boolean last = (page >= totalPages - 1); // 마지막 페이지 인덱스(6)와 비교
```

### BoardResponse.java (Paging DTO)
```java
// 페이지 번호 리스트 생성 (Mustache 반복문용)
this.pageNumbers = new java.util.ArrayList<>();
for (int i = 0; i < totalPages; i++) {
    this.pageNumbers.add(i);
}
```

## 3. 🍦 상세비유 (Easy Analogy)
> "이번 작업은 **'책의 맨 뒷장 번호를 확인하고 목차를 만드는 것'**과 같습니다. 이전에는 책장이 몇 장인지도 모르고 그냥 다음 장만 넘겼다면, 이제는 책 끝이 어디인지 정확히 알고(`totalCount`), 책 하단에 `1, 2, 3...` 쪽 번호를 써넣어 원하는 곳으로 바로 펼쳐볼 수 있게 만든 거죠. 물론 마지막 장에 도달하면 '다음 장'으로 넘어가는 화살표도 친절하게 지워두었습니다!"

## 4. 📚 기술 딥다이브 (Technical Deep-dive)
- **Math.ceil()**: 정수 나눗셈은 소수점을 버리므로, `double`로 형변환 후 올림 처리를 해야 데이터가 1개라도 남았을 때 마지막 페이지가 정상적으로 생성됩니다.
- **Dynamic List Generation**: 서버에서 리스트를 미리 만들어 전달함으로써, Mustache 같은 단순한 템플릿 엔진에서도 복잡한 루프 UI를 쉽게 구현할 수 있도록 'View 전용 데이터'를 가공하는 기법입니다.
- **Hard-coded vs Dynamic**: 현재 페이지 크기(3)를 상수로 사용했지만, 실제 서비스에서는 설정 파일이나 파라미터로 관리하여 유연성을 높이는 것이 일반적입니다.
