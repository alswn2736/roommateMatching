<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/jspf/group-preview.jspf" %>
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
    <jsp:include page="/views/modal/styles.jsp" />
</head>
<body class="group-page">
    <jsp:include page="/views/common/header.jsp" />
    <main class="group-main">
        <nav class="group-breadcrumb" aria-label="현재 위치"><span>룸메이트 홈</span><span aria-hidden="true">&gt;</span><strong>기숙사 매칭 그룹</strong></nav>
        <c:choose>
        <c:when test="${not empty requestScope.groupView}">
        <section class="group-summary" aria-labelledby="group-title">
            <div><div class="group-tags"><span><c:out value="${requestScope.groupView.dormitory}" /></span><span><c:out value="${requestScope.groupView.room}" /></span></div>
                <h1 id="group-title"><c:out value="${requestScope.groupView.name}" /></h1></div>
            <c:if test="${requestScope.groupPreview}"><div class="group-members" aria-label="그룹 멤버 2명">
                <img src="${pageContext.request.contextPath}/assets/image/group/member-1.png" alt="">
                <img src="${pageContext.request.contextPath}/assets/image/group/member-2.png" alt=""><span>+0</span>
            </div></c:if>
            <c:if test="${not requestScope.groupPreview and not empty requestScope.groupView.members}">
                <ul class="group-members" aria-label="그룹 멤버"><c:forEach var="member" items="${requestScope.groupView.members}"><li><c:out value="${member.name}" /></li></c:forEach></ul>
            </c:if>
        </section>
        <c:if test="${not empty requestScope.groupView.notice}"><div class="group-notice"><img src="${pageContext.request.contextPath}/assets/image/group/megaphone.svg" width="20" height="20" alt=""><p><c:out value="${requestScope.groupView.notice}" /></p></div></c:if>
        <nav class="group-tabs nav" aria-label="그룹 항목">
            <button type="button" class="active" data-group-tab="all" aria-pressed="true">전체 홈</button>
            <button type="button" data-group-tab="roles" aria-pressed="false">역할 분담</button>
            <button type="button" data-group-tab="rules" aria-pressed="false">약속 규칙</button>
            <button type="button" data-group-tab="bills" aria-pressed="false">공과금 정산</button>
        </nav>
        <p class="group-feedback" data-group-feedback role="status" hidden></p>
        <div class="group-grid">
            <div class="group-left">
                <section class="group-card" data-group-section="roles" aria-labelledby="roles-title">
                    <div class="group-card-heading"><h2 id="roles-title">역할 분담 및 당번</h2><button type="button" data-group-action="edit-roles">수정하기</button></div>
                    <ul class="group-role-list"><c:forEach var="role" items="${requestScope.groupView.roles}"><li>
                        <span class="group-role-icon" aria-hidden="true"><c:out value="${role.icon}" default="🧹" /></span><div><h3><c:out value="${role.title}" /></h3><p><c:out value="${role.assignment}" /></p></div>
                    </li></c:forEach></ul>
                    <c:if test="${empty requestScope.groupView.roles}"><p class="group-empty">등록된 역할이 없습니다.</p></c:if>
                </section>
                <section class="group-card" data-group-section="rules" aria-labelledby="rules-title">
                    <div class="group-card-heading"><h2 id="rules-title">약속 규칙 리스트</h2><button type="button" data-group-action="add-rule">규칙 추가 +</button></div>
                    <ol class="group-rule-list"><c:forEach var="rule" items="${requestScope.groupView.rules}" varStatus="status"><li>
                        <span class="group-rule-number"><fmt:formatNumber value="${status.count}" pattern="00" /></span><span class="group-rule-text"><c:out value="${rule.text}" /></span>
                        <span class="group-badge ${rule.agreed ? 'group-badge-agreed' : ''}"><c:out value="${rule.agreed ? '전원 동의' : '동의 대기'}" /></span>
                    </li></c:forEach></ol>
                    <c:if test="${empty requestScope.groupView.rules}"><p class="group-empty">등록된 약속 규칙이 없습니다.</p></c:if>
                </section>
            </div>
            <section class="group-card group-bills" data-group-section="bills" aria-labelledby="bills-title">
                <div class="group-card-heading"><h2 id="bills-title">공과금 및 정산 현황</h2><button type="button" data-group-action="add-bill">정산 등록</button></div>
                <div class="group-stats"><div><span>이번 달 총액</span><strong><fmt:formatNumber value="${empty requestScope.groupView.monthlyTotal ? 0 : requestScope.groupView.monthlyTotal}" pattern="#,##0" />원</strong></div>
                    <div><span>정산 미완료</span><strong class="group-accent"><c:out value="${empty requestScope.groupView.unsettledCount ? 0 : requestScope.groupView.unsettledCount}" />건</strong></div></div>
                <ul class="group-bill-list"><c:forEach var="bill" items="${requestScope.groupView.bills}"><li><div><h3><c:out value="${bill.title}" /></h3>
                    <p>총 <fmt:formatNumber value="${bill.amount}" pattern="#,##0" />원 • 인당 <fmt:formatNumber value="${bill.perPerson}" pattern="0" />원</p></div>
                    <span class="group-badge ${bill.settled ? '' : 'group-badge-pending'}"><c:out value="${bill.settled ? '정산 완료' : '미정산'}" /></span>
                </li></c:forEach></ul>
                <c:if test="${empty requestScope.groupView.bills}"><p class="group-empty">등록된 정산 내역이 없습니다.</p></c:if>
                <c:if test="${not empty requestScope.groupView.settlementMessage}"><div class="group-settlement"><h3>이번 달 최종 정산 알림</h3><p><c:out value="${requestScope.groupView.settlementMessage}" /></p></div></c:if>
            </section>
        </div>
        <input type="hidden" data-group-id value="<c:out value='${requestScope.groupView.id}' />">
        </c:when>
        <c:otherwise><section class="group-card group-empty-state"><h1>아직 연결된 그룹이 없습니다.</h1><p>매칭된 그룹 정보가 준비되면 이곳에서 역할, 약속 규칙, 정산 내역을 확인할 수 있습니다.</p></section></c:otherwise>
        </c:choose>
        <c:if test="${requestScope.groupPreview}"><aside class="group-preview-tools" aria-label="화면 미리보기">
            <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#roommate-leave">룸메 탈퇴 요청</button>
            <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#roommate-withdraw">사이트 탈퇴</button>
            <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#roommate-photo">프로필 사진 변경</button>
        </aside></c:if>
        <noscript><p>항목 전환과 모달을 사용하려면 JavaScript를 켜주세요.</p></noscript>
    </main>
    <jsp:include page="/views/modal/leave.jsp" />
    <jsp:include page="/views/modal/withdraw.jsp" />
    <jsp:include page="/views/modal/photo.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/group/detail.js" defer></script>
    <script src="${pageContext.request.contextPath}/assets/js/modal/modal.js" defer></script>
</body>
</html>
