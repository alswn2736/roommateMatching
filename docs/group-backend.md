# 그룹 백엔드 구현 현황

2026년 11월 28일 기준. 팀 `test` 브랜치의 로그인·마이페이지 변경을 반영했습니다.

## 구현 범위

| 기능 | 구현 내용 |
| --- | --- |
| 그룹 조회 | 로그인한 회원의 그룹 목록, 구성원, 역할, 생활 규칙, 퇴실 요청 조회 |
| 역할 관리 | 역할 등록·수정·삭제, 담당 구성원 지정 |
| 생활 규칙 | 규칙 등록·수정·삭제, 구성원 동의 처리 |
| 퇴실 | 사유 입력, 다른 구성원의 동의·거절, 승인 완료 시 소속 정리 |
| 그룹 편집 모달 | 입력 폼 표시, 저장 요청, 오류 안내와 입력값 복구 |
| 마이페이지 연동 | 정원 충족 시 그룹 생성, 퇴실 요청 연결, 그룹 상세 이동 |

공과금과 공지는 이번 그룹 화면의 구현 범위에서 제외했습니다. 팀 공통 정산 DAO·DTO는 유지했습니다.

## 사용 기술과 처리 구조

- **서버:** Java 21, Servlet 6.0, Tomcat 11
- **화면:** JSP, JSTL·EL, JavaScript, HTML `dialog`
- **DB 처리:** Oracle JDBC, MyBatis 3.5.19, MyBatis `PooledDataSource` 연결 풀
- **트랜잭션:** JDBC와 MyBatis가 같은 Connection을 사용하며, 관련 작업이 모두 성공하면 commit하고 하나라도 실패하면 rollback합니다.

`GroupServlet`이 요청을 받고, `GroupService`에서 권한과 처리 조건을 확인한 뒤 DAO를 통해 DB를 조회·수정합니다. 조회 결과는 `GroupPage`에 담아 JSP로 전달합니다.

그룹 모달은 일반 POST 폼으로 저장합니다. 저장이 끝나면 상세 화면으로 이동해 최신 내용을 다시 조회하고, 입력 오류가 있으면 작성한 값을 유지한 채 해당 모달을 다시 엽니다.

## 로그인·마이페이지 연동

- 로그인에서 저장한 `loginMember` 세션의 `model.domain.User`를 사용합니다. 기존 `UserDTO` 세션도 지원합니다.
- 신청 수락·거절 시 모집글 행을 잠근 뒤 신청 상태와 남은 정원을 다시 확인합니다. 정원이 채워지면 같은 트랜잭션에서 그룹을 생성합니다.
- 그룹 번호는 모집글 번호를 사용합니다. 그룹 생성에 실패하면 신청 수락과 모집 마감도 함께 취소합니다.
- 마이페이지에서도 그룹 서비스의 퇴실 요청 처리를 사용합니다. 사유는 300자까지 입력하며, 요청자를 제외한 구성원의 응답 항목을 함께 생성합니다.
- 활동 중인 그룹이 있는 회원은 계정 탈퇴 전에 그룹에서 퇴실하도록 안내합니다.
- 공통 MyBatis 설정에 그룹 매퍼와 `localCacheScope=STATEMENT`를 추가했습니다. JDBC로 수정한 뒤 이전 조회 결과가 남는 것을 방지하기 위한 설정입니다.

## 주요 처리 기준

규칙 내용이 바뀌면 구성원에게 동의를 다시 받습니다. 같은 내용이나 앞뒤 공백만 변경하면 기존 동의를 유지합니다.

그룹별로 진행 중인 퇴실 요청은 하나만 허용합니다. 다른 구성원 모두가 동의하면 퇴실하고, 한 명이라도 거절하면 소속을 유지합니다. 요청 이후 구성원이 바뀐 경우에는 기존 응답만으로 승인하지 않습니다. 마지막 구성원의 퇴실은 제한합니다.

현재 퇴실자의 역할은 마지막 승인자에게 넘깁니다. 소속 삭제 시 외래 키에 연결된 규칙 동의와 과거 퇴실 응답도 정리합니다. 이 역할 인계 방식과 이력 보존 범위는 팀에서 확정할 필요가 있습니다.

그룹 구성원만 접근할 수 있고, 종료된 그룹은 수정할 수 없습니다. 그룹의 변경 요청에는 CSRF 토큰을 검사하며, 사용자 입력은 JSP의 `c:out`으로 출력합니다.

마이페이지의 신청 수락·거절 및 퇴실 요청에도 동일한 세션 CSRF 토큰을 검사합니다. 해당 화면에서 발급된 토큰을 `X-CSRF-Token` 헤더로 보내며, 없거나 다른 세션의 토큰이면 DB 처리 전에 403으로 거절합니다.

## 실행과 연동 경로

프로젝트 루트에서 `mvn clean verify`를 실행합니다. 생성되는 `target/roommateMatching-0.0.1-SNAPSHOT.war`를 Tomcat에 배포하고 context path는 `/roommateMatching`으로 설정합니다. Eclipse에서는 Maven Update Project 후 `src/main/resources`가 배포에 포함되는지 확인합니다.

그룹 DB 연결에는 Tomcat 프로세스의 `GROUP_DB_URL`, `GROUP_DB_USER`, `GROUP_DB_PASSWORD` 환경변수가 필요합니다. 로그인·마이페이지는 비밀번호에 `ROOMMATE_DB_PASSWORD`를 사용합니다. 각 영역이 같은 팀 DB에 연결되도록 설정해야 합니다.

| 경로 | 메서드 | 용도 |
| --- | --- | --- |
| `/app/matched`, `/app/group/list` | GET | 내 그룹 목록 |
| `/app/group?groupId=그룹번호` | GET | 그룹 상세 |
| `/app/group/role` | POST | 역할 저장·삭제 |
| `/app/group/rule` | POST | 규칙 저장·삭제 |
| `/app/group/rule/agree` | POST | 규칙 동의 |
| `/app/group/leave` | POST | 퇴실 요청 |
| `/app/group/leave/consent` | POST | 퇴실 동의·거절 |

## 최신 테스트

로컬 H2 환경에서 그룹 기능, 로그인 세션 연동, 마이페이지 신청 수락과 그룹 생성, 퇴실 요청과 승인, 실패 시 rollback, 동시 신청 수락을 확인했습니다. HTTP 요청과 JSP 응답, WAR 패키징 검사도 통과했습니다. 이 결과는 그룹 및 연동 테스트 범위이며, 팀 전체 기능이나 실제 Oracle 연결 검증을 의미하지는 않습니다.

11월 28일 재검토에서 Java 21의 Maven `clean verify`로 서비스·HTTP 테스트 21개와 WAR 통합 테스트 1개가 통과했습니다. 마이페이지 그룹 변경 경로는 누락·잘못된 토큰과 다른 세션의 토큰을 거절하고, 유효한 AJAX 토큰은 입력 검사로 진행하는 회귀 시나리오를 추가했습니다.

## 팀 확인 사항

- **팀 DB 적용:** `db/group/00_inspect_read_only.sql`로 실제 컬럼·PK/FK·상태값을 확인해야 합니다. 기존 그룹 번호가 모집글 번호와 다르면 데이터 처리 방식을 먼저 정해야 합니다.
- **시퀀스와 입력 길이:** `RM_ROLE_SEQ`, `RM_RULE_SEQ`, `RM_LEAVE_SEQ`가 필요합니다. 기존 시퀀스·트리거와 중복 여부를 확인하고, 역할·규칙 100자와 퇴실 사유 300자가 실제 Oracle 컬럼에 저장되는지도 확인해야 합니다. BYTE 기준 컬럼은 한글 입력 시 길이 차이가 있습니다.
- **공통 처리:** 다른 경로에서 소속을 변경할 때도 같은 그룹 잠금 규칙을 맞춰야 합니다. 계정 탈퇴와 새 그룹 가입이 동시에 발생하는 경우에 대한 공통 처리, 그룹 연동 외 마이페이지 변경 요청(사진·계정·채팅 등)의 CSRF 적용이 남아 있습니다.
- **운영 정책과 화면:** 퇴실 시 역할 인계·이력 보존, 계정 탈퇴 시 종료 그룹과 모집글 이력 처리, 공통 헤더의 샘플 이름·사진 교체를 담당자와 확인해야 합니다.
- **DB 접속 정보:** 과거 원격 코드에 포함됐던 비밀번호는 DB 담당자가 교체해야 합니다.

팀 Oracle에는 스키마 변경이나 시퀀스 생성 스크립트를 실행하지 않았습니다. `src/test/resources/group/schema.sql`은 로컬 H2 테스트 전용이며 팀 DB에 적용하지 않습니다.

## 회의 전달 문서 보완

- 공통 로그인 세션 사용과 신청 수락 후 그룹 생성은 코드 및 로컬 통합 테스트까지 연결된 상태입니다. 남은 확인은 실제 팀 Oracle과 배포 환경에서의 전체 사용자 흐름입니다.
- 공통 사진·사이트 탈퇴 모달의 미리보기와 확인 문구 UI는 마이페이지의 실제 사진 저장·계정 삭제 기능과 구분합니다. 마이페이지에는 해당 백엔드가 있으나 공통 모달 연결 완료를 의미하지 않습니다.
- 자동 검사는 그룹 및 마이페이지 그룹 연동 범위입니다. `src/test/java/test/auth`의 main 실행형 프로그램은 Maven 테스트 성공에 포함되지 않습니다.
- PR 자동 검사 설정은 `.github/workflows/group-verify.yml`에 있습니다. GitHub 실행 결과는 해당 커밋의 Actions에서 별도로 확인해야 합니다.

## 참고자료

- [Servlet과 JSP 데이터 전달 실습](https://byeol-dev.tistory.com/entry/jsp-servlet-페이지-넘김-데이터-받기-실습): 요청값과 JSP 응답 흐름 검토
- [MyBatis commit·rollback 사례](https://myhappyman.tistory.com/149), [MyBatis DefaultSqlSession 소스](https://github.com/mybatis/mybatis-3/blob/mybatis-3.5.19/src/main/java/org/apache/ibatis/session/defaults/DefaultSqlSession.java): 수동 트랜잭션과 강제 commit·rollback 처리 검토
- [MDN dialog 예제](https://github.com/mdn/content/blob/main/files/en-us/web/api/htmldialogelement/showmodal/index.md): 그룹 편집 모달의 열기·닫기 동작 검토
- [Bootstrap Modal](https://getbootstrap.com/docs/5.2/components/modal/): 공통 모달의 이벤트 처리 검토
- [W3C Dialog Pattern](https://www.w3.org/WAI/ARIA/apg/patterns/dialog-modal/): 모달의 키보드 이동과 포커스 복귀 검토
- [MDN FileReader](https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL): 사진 변경 모달의 파일 미리보기 검토
