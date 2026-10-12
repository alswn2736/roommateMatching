# 그룹 화면 및 공통 모달 연결

## 파일과 의존성

- 화면: `src/main/webapp/views/group/detail.jsp`
- 모달 조각: `views/modal/leave.jsp`, `withdraw.jsp`, `photo.jsp`
- 모달 스타일 조각: `views/modal/styles.jsp`를 부모 페이지의 head에서 한 번 포함
- 동작: `assets/js/group/detail.js`, `assets/js/modal/modal.js`
- Bootstrap 5.2.3 CSS 및 bundle JS를 부모 페이지에서 한 번 로드
- 기존 `views/common/header.jsp`, `assets/css/common/header.css` 재사용
- 기존 Jakarta JSTL 3 라이브러리 사용. 추가 JAR 없음

`styles.jsp`와 모달 JSP는 독립 페이지가 아니라 include 조각이다. 각 모달은 페이지에 한 번만 포함한다. 기존 화면에 이미 Bootstrap bundle이 있으면 중복 로드하지 않는다. 스타일은 `.group-page`, `.group-main`, `.roommate-modal` 내부로 제한했다.

## 그룹 조회 데이터

컨트롤러가 `request.setAttribute("groupView", model)`을 호출한 후 `/views/group/detail.jsp`로 forward한다. model은 Map 또는 아래 프로퍼티의 getter가 있는 DTO를 사용한다. 모델이 없으면 빈 그룹 안내를 표시한다.

| 프로퍼티 | 타입 | 용도 |
| --- | --- | --- |
| id | String 또는 숫자 | 그룹 식별자 |
| name, dormitory, room | String | 그룹명, 기숙사, 호실 |
| matchedDate | String | 표시용 매칭일 |
| notice | String | 일반 텍스트 공지; 비어 있으면 숨김 |
| members | List | 요소: `name` 문자열; 실제 멤버 이름 표시 |
| roles | List | 요소: `icon`, `title`, `assignment` 문자열 |
| rules | List | 요소: `text` 문자열, `agreed` Boolean |
| bills | List | 요소: `title` 문자열, `amount`, `perPerson` 숫자, `settled` Boolean |
| monthlyTotal | 숫자 | 서버에서 집계한 이번 달 총액 |
| unsettledCount | 정수 | 서버에서 집계한 미정산 건수 |
| settlementMessage | String | 표시용 정산 안내; 비어 있으면 숨김 |

목록은 빈 리스트, 문자열은 빈 문자열로 제공할 수 있다. 금액에는 `"24,000원"` 같은 문자열 대신 `24000`처럼 숫자를 전달한다. 텍스트는 `c:out`으로 출력한다. 정산 금액 계산과 동의 여부 판단은 서버에서 수행한다.

`?preview=1`로 접속하고 컨트롤러 모델이 없을 때만 `WEB-INF/jspf/group-preview.jspf`의 샘플 데이터를 사용한다. 샘플 데이터는 DB·세션·localStorage에 저장하지 않는다. 모델이 있으면 URL에 preview가 있어도 실제 모델을 우선한다. 실제 서비스 배포 시 detail.jsp의 preview include 및 검토용 도구 영역을 제거할 수 있다.

## 그룹 버튼

버튼은 `roommate:group-action` 이벤트를 발생시킨다. `event.detail`은 `{ action, groupId }`이다.

| action | 버튼 |
| --- | --- |
| edit-roles | 수정하기 |
| add-rule | 규칙 추가 + |
| add-bill | 정산 등록 |

상위 페이지 또는 별도 연결 스크립트에서 이벤트를 받고 `event.preventDefault()`를 호출한 다음, 팀에서 정한 편집 화면/컨트롤러로 연결한다. 편집 폼 디자인과 URL이 아직 없어 여기서는 임의로 만들지 않았다. 연결하지 않은 경우 서버 연결 대기 안내를 표시한다.

## 모달 포함 예시

```jsp
<!-- 부모 페이지 head: Bootstrap CSS 다음에 -->
<jsp:include page="/views/modal/styles.jsp" />

<!-- 부모 페이지 body -->
<button type="button" data-bs-toggle="modal" data-bs-target="#roommate-leave">룸메 탈퇴 요청</button>
<jsp:include page="/views/modal/leave.jsp" />

<!-- Bootstrap bundle 다음에, 페이지당 한 번 -->
<script src="${pageContext.request.contextPath}/assets/js/modal/modal.js" defer></script>
```

다른 모달의 target은 `#roommate-withdraw`, `#roommate-photo`이다. 부모가 페이지 전체 HTML, Bootstrap CSS/bundle, 폰트를 제공해야 한다. 모달 include에 body/head를 중복 작성하지 않는다. 마이페이지에서 재사용할 때 해당 페이지 담당자가 원하는 위치에 버튼과 필요한 모달만 포함한다.

leave는 같은 `requestScope.groupView`의 `id`, `name`, `matchedDate`를 참조한다. groupId가 없으면 제출을 비활성화한다. photo는 `requestScope.profilePhotoPath`로 서버가 검증한 애플리케이션 내부 이미지 경로(예: `/uploads/profile/123.png`)를 받는다. 없으면 기존 기본 아바타를 표시한다. 요청 파라미터를 그대로 이미지 경로로 전달하지 않는다.

## 모달 제출 연결

모든 모달은 `roommate:modal-submit` 이벤트를 발생시킨다. 프론트 파일에 API 주소를 가정하거나 실제 회원 삭제 로직을 넣지 않았다.

`event.detail`:

- `action`: `leave`, `withdraw`, `photo`
- `formData`: 브라우저 FormData
- `complete({ success, message })`: 처리 결과와 사용자 안내를 반환하는 콜백

| action | formData 항목 |
| --- | --- |
| leave | groupId, reason(최대 1000자), csrfToken |
| withdraw | confirmation(정확히 `탈퇴합니다`), csrfToken |
| photo | photo(중앙 크롭한 512×512 PNG 파일), csrfToken |

csrfToken은 컨트롤러에서 request 속성으로 제공한다. 서버가 다른 CSRF 필드명/헤더 방식을 사용한다면 연결 스크립트에서 맞춘다. 사진 zoom은 파일에 반영되므로 서버에서 다시 크롭하지 않는다. 원본 파일은 전송하지 않는다.

```javascript
// submitToServer는 팀의 실제 컨트롤러 URL과 응답 규격에 맞춰 구현한다.
document.addEventListener('roommate:modal-submit', async event => {
    const { action, formData, complete } = event.detail;
    event.preventDefault();
    try {
        const result = await submitToServer(action, formData);
        complete({ success: result.success, message: result.message });
        // 성공 후에는 필요에 따라 서버 데이터를 다시 조회한다.
    } catch (error) {
        complete({ success: false, message: '처리하지 못했습니다. 다시 시도해주세요.' });
    }
});
```

이 예시는 연결 방식 설명이며 정의되지 않은 함수를 그대로 붙여 넣어 실행하지 않는다. 연결 담당자는 성공·실패 모두 `complete`를 호출해야 한다. 처리 중에는 재제출과 모달 닫기가 잠긴다. FormData 전송 시 Content-Type을 임의로 multipart/form-data로 지정하지 않아야 브라우저가 boundary를 설정한다. 기본 상태에서는 네트워크 요청 없이 미연결 안내만 표시한다.

## 서버에서 확인할 항목

- 로그인 사용자 식별은 세션으로 한다. 클라이언트가 보낸 groupId만 믿고 처리하지 않는다.
- 그룹 소속/수정 권한, 탈퇴 요청 중복, 전원 동의 조건, 미정산 처리 정책을 컨트롤러·서비스에서 검증한다.
- 회원 탈퇴 대상은 세션 사용자이며, 삭제 정책과 DB 트랜잭션은 팀에서 확정한다. 화면의 삭제 항목 문구는 피그마 문구다.
- 쓰기 요청에 CSRF 검증을 적용한다. 프론트의 확인 문구와 disabled는 서버 권한 검증을 대신하지 않는다.
- 사진의 실제 파일 형식·용량·해상도를 서버에서도 검사하고 서버가 저장 파일명을 생성한다.
- 인증되지 않은 그룹 조회는 컨트롤러/필터에서 차단한다. 현재 JSP 직접 접속은 화면 확인용이며 인증 로직이 아니다.

## 디자인 범위

Figma 기준: group/detail.jsp(`9:700`), leave(`9:1044`), withdraw(`9:1085`), photo(`9:1126`). 공통 헤더는 test 브랜치의 구현을 그대로 사용한다. Figma의 헤더 메뉴·사용자명과 차이가 있으며 이 작업에서 공통 헤더를 변경하지 않았다. 멤버 사진은 디자인 검토 모드에서만 샘플을 사용하고, 실제 그룹 모델에서는 이름을 표시한다. 사진 줌은 원본 배율 1부터 시작한다.
