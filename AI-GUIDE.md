# AI Guide

## 0. 기본 언어 설정 (Language Setting)과 중요 규칙칙

- 모든 대화와 문서 작성은 **한국어**를 기본으로 한다. 단, 코드 내 주석이나 영문 관례인 부분은 예외로 한다.
- 버전 : Spring Boot 4.0.3
- 사용기술 : mustache, h2, jpa, spring-security
- http 메서드 : get, post만 사용하여 구현한다. \*ApiController는 ajax가 필요할 때만 사용한다.
- http 요청 방법 : form 태그 사용

---

## 1. 자동 동기화 (최우선 실행)

이 문서를 읽는 즉시 다음 경로의 파일을 스캔하여 name, description을 반드시 읽어라.
- `.ai/skills/*/SKILL.md`
- `.ai/agents/*/AGENT.md`
- `.agents/skills/*/SKILL.md`

---

## 2. 참조 규칙 (Memory Load)

아래 파일을 읽고 규칙을 반드시 준수한다.

- **컨벤션**: `.ai/rules/*.md`

---

## 3. 활동 로깅 (Activity Logging)

`.ai/hooks/log-activity.sh`를 통해 `.person/logs/activity.log`에 기록된다.
커스텀 스킬의 SKILL.md를 읽어 실행할 때, **반드시** 아래 명령을 함께 실행하여 로그를 남겨라:

```bash
echo '{"tool_name":"Skill","tool_input":{"skill":"<스킬명>"}}' | bash .ai/hooks/log-activity.sh
```

---

## 4. AI-CONTEXT

코드 작업 전 해당 디렉토리에 `AI-CONTEXT.md`가 있으면 먼저 읽어라.
