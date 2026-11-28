# 룸메이트 매칭

Java 21과 Maven으로 빌드하는 JSP/Servlet 웹 애플리케이션입니다. 이 브랜치는 그룹 백엔드와 기존 로그인·마이페이지 연동을 포함합니다.

## 빌드와 테스트

```sh
mvn -B clean verify
```

그룹 서비스, 마이페이지 그룹 생성·퇴실 연동, HTTP/JSP 응답, WAR 패키징을 로컬 H2와 embedded Tomcat으로 검사합니다. 실제 팀 Oracle에 접속하거나 스키마를 변경하지 않습니다. 기존 auth의 `main` 실행형 점검 프로그램은 이 자동 테스트에 포함되지 않습니다.

생성 파일은 `target/roommateMatching-0.0.1-SNAPSHOT.war`입니다. Tomcat 11에 `/roommateMatching` context path로 배포합니다.

## 팀 환경 연결

Tomcat 실행 환경에 다음 값을 설정해야 합니다. 비밀번호를 코드나 Git에 넣지 않습니다.

- 그룹: `GROUP_DB_URL`, `GROUP_DB_USER`, `GROUP_DB_PASSWORD`
- 로그인·마이페이지: `ROOMMATE_DB_PASSWORD` 및 각 영역의 DB 연결 설정

모든 영역이 같은 팀 DB를 사용해야 합니다. DB 담당자와 실제 컬럼, PK/FK, 상태값, 문자열 BYTE/CHAR 길이, `RM_ROLE_SEQ`·`RM_RULE_SEQ`·`RM_LEAVE_SEQ`를 확인한 뒤 연결합니다. `db/group/00_inspect_read_only.sql`은 조회용이며, 시퀀스 생성 파일은 담당자 확인 후 별도로 적용합니다. 테스트용 `src/test/resources/group/schema.sql`을 팀 DB에 실행하지 않습니다.

## 그룹 기능과 검토 범위

로그인 후 `/app/matched`에서 그룹을 선택할 수 있습니다. 역할 관리, 생활 규칙과 동의, 퇴실 요청·응답을 제공하며, 마이페이지의 신청 수락으로 정원이 차면 그룹을 생성합니다.

그룹 변경 요청과 마이페이지의 신청 수락·거절 및 퇴실 요청에는 세션 CSRF 토큰이 필요합니다. 기존 그룹 폼은 `csrf` 필드, 마이페이지 JavaScript는 `X-CSRF-Token` 헤더로 전송합니다.

공과금·공지는 이번 그룹 구현 범위에 포함되지 않습니다. 퇴실자의 역할을 마지막 승인자에게 넘기는 정책, 과거 동의·응답 정리 범위, 마지막 구성원의 퇴실 정책은 팀에서 확정해야 합니다. 실제 Oracle 환경의 전체 연동 검증은 별도로 필요합니다.

구체적인 구현과 회의 확인 항목은 [그룹 백엔드 문서](docs/group-backend.md)에 정리돼 있습니다.
