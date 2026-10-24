<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>기숙사 매칭 그룹 | 룸메이트</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&family=Noto+Sans+KR:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/common/header.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/group/detail.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/modal/modal.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/modal/leave.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/modal/withdraw.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/modal/photo.css" rel="stylesheet">
</head>
<body class="group-page">
    <jsp:include page="/views/common/header.jsp" />
    <main class="group-main">
        <nav class="group-breadcrumb" aria-label="현재 위치">
            <span>룸메이트 홈</span>
            <span aria-hidden="true">&gt;</span>
            <strong>기숙사 매칭 그룹</strong>
        </nav>

        <section class="group-summary" aria-labelledby="group-title">
            <div>
                <div class="group-tags">
                    <span>A동 기숙사 2인실</span>
                    <span>3층 302호</span>
                </div>
                <h1 id="group-title">A동 302호 우리들의 따뜻한 단짝방</h1>
            </div>
            <div class="group-members" aria-label="그룹 멤버 2명">
                <img src="${pageContext.request.contextPath}/assets/image/group/member-1.png" alt="">
                <img src="${pageContext.request.contextPath}/assets/image/group/member-2.png" alt="">
                <span>+0</span>
            </div>
        </section>

        <div class="group-notice">
            <img src="${pageContext.request.contextPath}/assets/image/group/megaphone.svg" width="20" height="20" alt="">
            <p>[공지] 이번 주말 일요일에 에어컨 필터 물청소 같이 하기로 약속했습니다! (14:00 개시)</p>
        </div>

        <nav class="group-tabs nav" aria-label="그룹 항목">
            <button type="button" class="active" data-group-tab="all" aria-pressed="true">전체 홈</button>
            <button type="button" data-group-tab="roles" aria-pressed="false">역할 분담</button>
            <button type="button" data-group-tab="rules" aria-pressed="false">약속 규칙</button>
            <button type="button" data-group-tab="bills" aria-pressed="false">공과금 정산</button>
        </nav>
        <p class="group-feedback" id="group-feedback" role="status" hidden></p>

        <div class="group-grid">
            <div class="group-left">
                <section class="group-card" data-group-section="roles" aria-labelledby="roles-title">
                    <div class="group-card-heading">
                        <h2 id="roles-title">역할 분담 및 당번</h2>
                        <button type="button" class="group-edit-button">수정하기</button>
                    </div>
                    <ul class="group-role-list">
                        <li>
                            <span class="group-role-icon" aria-hidden="true">🧹</span>
                            <div>
                                <h3>바닥 쓸기 &amp; 쓰레기 비우기</h3>
                                <p>김민재 (매주 월/목)</p>
                            </div>
                        </li>
                        <li>
                            <span class="group-role-icon" aria-hidden="true">🧼</span>
                            <div>
                                <h3>욕실 청소 &amp; 하수구 정리</h3>
                                <p>박지훈 (매주 수/토)</p>
                            </div>
                        </li>
                        <li>
                            <span class="group-role-icon" aria-hidden="true">♻️</span>
                            <div>
                                <h3>분리수거 배출</h3>
                                <p>공동 분담 (주말 수시)</p>
                            </div>
                        </li>
                    </ul>
                </section>

                <section class="group-card" data-group-section="rules" aria-labelledby="rules-title">
                    <div class="group-card-heading">
                        <h2 id="rules-title">약속 규칙 리스트</h2>
                        <button type="button" class="group-edit-button">규칙 추가 +</button>
                    </div>
                    <ol class="group-rule-list">
                        <li>
                            <span class="group-rule-number">01</span>
                            <span class="group-rule-text">밤 12시 이후 스탠드 조명 제외 전체 소등하기</span>
                            <span class="group-badge group-badge-agreed">전원 동의</span>
                        </li>
                        <li>
                            <span class="group-rule-number">02</span>
                            <span class="group-rule-text">외부인(친구 등) 데려올 때는 최소 1일 전 상호 공유</span>
                            <span class="group-badge group-badge-agreed">전원 동의</span>
                        </li>
                        <li>
                            <span class="group-rule-number">03</span>
                            <span class="group-rule-text">음식물 쓰레기는 발생 당일 즉시 외부 분리 수거함 배출</span>
                            <span class="group-badge group-badge-agreed">전원 동의</span>
                        </li>
                    </ol>
                </section>
            </div>

            <section class="group-card group-bills" data-group-section="bills" aria-labelledby="bills-title">
                <div class="group-card-heading">
                    <h2 id="bills-title">공과금 및 정산 현황</h2>
                    <button type="button" class="group-edit-button">정산 등록</button>
                </div>
                <div class="group-stats">
                    <div>
                        <span>이번 달 총액</span>
                        <strong>48,200원</strong>
                    </div>
                    <div>
                        <span>정산 미완료</span>
                        <strong class="group-accent">1건</strong>
                    </div>
                </div>
                <ul class="group-bill-list">
                    <li>
                        <div>
                            <h3>2월 가스비 공동 고지</h3>
                            <p>총 24,000원 • 인당 12000원</p>
                        </div>
                        <span class="group-badge">정산 완료</span>
                    </li>
                    <li>
                        <div>
                            <h3>생활 소모품 (공동 휴지, 세제)</h3>
                            <p>총 24,200원 • 인당 12100원</p>
                        </div>
                        <span class="group-badge group-badge-pending">미정산</span>
                    </li>
                </ul>
                <div class="group-settlement">
                    <h3>이번 달 최종 정산 알림</h3>
                    <p>김민재님이 지불한 소모품비로 인해, 박지훈 학우님이 12,100원을 송금해야 합니다.</p>
                </div>
            </section>
        </div>

        <c:if test="${param.preview == '1'}">
            <aside class="group-preview-tools" aria-label="화면 미리보기">
                <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#roommate-leave">룸메 탈퇴 요청</button>
                <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#roommate-withdraw">사이트 탈퇴</button>
                <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#roommate-photo">프로필 사진 변경</button>
            </aside>
        </c:if>
    </main>
    <jsp:include page="/views/modal/leave.jsp" />
    <jsp:include page="/views/modal/withdraw.jsp" />
    <jsp:include page="/views/modal/photo.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/group/detail.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/modal/modal.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/modal/photo.js" defer></script>
</body>
</html>
