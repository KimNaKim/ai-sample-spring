# 홈 페이지 재구축 보고서 (Home Page Rebuild Report)

## 1. 개요
단순한 텍스트 위주의 홈 페이지를 `frontend-design` 스킬을 활용하여 세련된 블로그 메인 화면으로 재구축함.

## 2. 주요 변경 사항

### 2.1 히어로 섹션 (Hero Section) 구현
- 서비스의 정체성을 보여주는 굵은 타이틀(`hero-title`)과 부드러운 설명 문구(`hero-subtitle`) 추가.
- `Pretendard` 폰트의 굵기(font-weight) 조절을 통해 시각적 위계 확립.

### 2.2 게시글 카드 그리드 (Post Card Grid) 구현
- `grid-template-columns`를 활용한 반응형 카드 리스트 설계.
- 각 카드는 `shadow-sm`, `radius-md`를 적용하여 입체감을 주었으며, 호버 시 `shadow-md`와 상단 이동(`translateY`) 효과로 인터랙션 강화.
- 뱃지(`badge`)와 더 보기 버튼(`btn-detail`)을 추가하여 정보 전달력 향상.

### 2.3 데이터 바인딩 및 예외 처리
- `{{#models}}`를 사용하여 게시글 리스트를 반복 렌더링하도록 구조화.
- `{{^models}}`를 활용하여 게시글이 없을 경우의 안내 문구 추가.

## 3. 기술적 특징
- **디자인 토큰 활용**: 모든 스타일 값(색상, 여백, 곡률 등)을 `design-system.md`에 정의된 변수만 사용하여 작성함.
- **Mustache Layout**: `{{> layout/header}}`, `{{> layout/footer}}`를 사용하여 일관된 레이아웃 유지.

## 4. 완료 확인
- [x] 홈 페이지 전용 스타일 추가 (`style.css`)
- [x] 히어로 섹션 및 카드 구조 설계 (`home.mustache`)
- [x] 데이터 바인딩 로직 적용
