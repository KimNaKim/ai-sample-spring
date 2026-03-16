# Design System Rules

## 1. 프론트엔드 작업 프로세스 (Mandatory)

프론트엔드 레이아웃 및 UI 작업 시 아래의 규칙을 반드시 준수한다.

- **공통 레이아웃 구성**: `header`, `footer`, `layout` 등 공통 레이아웃을 설계하거나 수정할 때는 반드시 `frontend-design` 스킬을 사용하여 디자인 토큰(CSS 변수) 기반으로 구성해야 한다.
- **Mustache 템플릿 생성**: 모든 `.mustache` 파일은 `frontend-design` 스킬을 통해 추출된 디자인 토큰을 참조하여 작성하며, 임의의 하드코딩된 스타일(예: `#ff0000`, `14px`) 사용을 금지한다.
- **일관성 유지**: 새로운 UI 요소를 추가할 때 기존 `.ai/rules/design-system.md`에 정의된 토큰이 있다면 이를 우선적으로 활용한다.

---

## 2. 디자인 토큰 (Design Tokens) - `:root` 정의용

| 토큰 분류 | 토큰명 | 값 | 설명 |
| :--- | :--- | :--- | :--- |
| **Color** | `--color-primary` | `#197fe6` | 브랜드 기본 컬러 (Blue) |
| | `--color-primary-hover` | `#1565c0` | 버튼/링크 호버 시 |
| | `--color-bg` | `#f8faff` | 전체 배경색 (Soft Gray-Blue) |
| | `--color-surface` | `#ffffff` | 카드, 네비게이션 배경 |
| | `--color-text-main` | `#1e293b` | 메인 텍스트 (Slate-800) |
| | `--color-text-muted` | `#64748b` | 보조 텍스트 (Slate-500) |
| | `--color-border` | `#e2e8f0` | 구분선, 테두리 |
| **Shape** | `--radius-sm` | `8px` | 입력창, 작은 버튼 |
| | `--radius-md` | `12px` | 기본 요소 (Card, Nav) |
| | `--radius-lg` | `20px` | 큰 섹션, 라운드 버튼 |
| **Shadow** | `--shadow-sm` | `0 2px 4px rgba(0,0,0,0.05)` | 은은한 깊이감 |
| | `--shadow-md` | `0 10px 15px -3px rgba(0,0,0,0.1)` | 메인 레이아웃 그림자 |
| **Spacing** | `--space-xs` | `8px` | 요소 간 좁은 여백 |
| | `--space-sm` | `16px` | 기본 여백 |
| | `--space-md` | `24px` | 섹션 내 여백 |
| | `--space-lg` | `48px` | 큰 영역 간 여백 |
| **Typography** | `--font-main` | `'Pretendard', system-ui, sans-serif` | 기본 폰트 |
