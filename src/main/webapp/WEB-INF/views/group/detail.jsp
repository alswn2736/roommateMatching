<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>우리 그룹 | 룸메이트</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/group/detail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/modal/leave.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/group/backend.css?v=20261124">
</head>
<body class="group-page">
<jsp:include page="/views/common/header.jsp"/>
<c:set var="active" value="${groupPage.group.status == 'ACTIVE'}"/>
<c:set var="pendingLeave" value="false"/>
<c:forEach items="${groupPage.leaveRequests}" var="request">
    <c:if test="${request.status == 'PENDING'}"><c:set var="pendingLeave" value="true"/></c:if>
</c:forEach>
<main class="group-main">
    <nav class="group-breadcrumb" aria-label="현재 위치">
        <a href="${pageContext.request.contextPath}/app/matched">내 매칭 그룹</a><span aria-hidden="true">&gt;</span><strong>기숙사 매칭 그룹</strong>
    </nav>
    <section class="group-summary">
        <div>
            <div class="group-tags">
                <span>${groupPage.group.academicYear}년 ${groupPage.group.semester}학기</span>
                <span>${groupPage.group.capacity}인 그룹</span>
            </div>
            <h1><c:out value="${groupPage.title}" default="우리 그룹"/></h1>
            <p class="group-subtitle">함께 생활할 약속과 역할을 정해 보세요.</p>
        </div>
        <div class="group-member-names" aria-label="그룹 구성원">
            <c:forEach items="${groupPage.members}" var="m">
                <span class="member-chip"><c:out value="${m.nickname}"/></span>
            </c:forEach>
        </div>
    </section>
    <c:if test="${not active}"><p class="backend-notice">종료된 그룹입니다. 저장된 내용을 조회할 수 있습니다.</p></c:if>
    <c:if test="${not empty groupMessage}"><p role="status" class="backend-notice"><c:out value="${groupMessage}"/></p></c:if>
    <c:if test="${not empty formError}"><p role="alert" class="form-error"><c:out value="${formError}"/></p></c:if>
    <nav class="group-tabs" aria-label="그룹 항목">
        <button type="button" class="active" data-section="all" aria-pressed="true">전체 홈</button>
        <button type="button" data-section="roles" aria-pressed="false">역할 분담</button>
        <button type="button" data-section="rules" aria-pressed="false">약속 규칙</button>
        <button type="button" data-section="leave" aria-pressed="false">퇴실 요청</button>
    </nav>
    <div class="group-grid">
        <div class="group-left">
            <section class="group-card" data-panel="roles">
                <div class="group-card-heading"><h2>역할 분담 및 당번</h2>
                    <c:if test="${active}"><button type="button" data-dialog="role-new">역할 추가 +</button></c:if>
                </div>
                <c:if test="${empty groupPage.roles}"><p class="group-empty">아직 정한 역할이 없어요.<br>함께 나눌 일을 추가해 보세요.</p></c:if>
                <ul class="group-role-list">
                    <c:forEach items="${groupPage.roles}" var="role">
                        <li><span class="group-role-icon" aria-hidden="true">🧹</span>
                            <div class="item-content"><h3><c:out value="${role.roleTitle}"/></h3><p>담당 · <c:out value="${groupPage.memberNames[role.assigneeId]}" default="이전 구성원"/></p></div>
                            <c:if test="${active}"><button type="button" class="text-button" data-dialog="role-${role.roleId}" aria-label="<c:out value='${role.roleTitle}'/> 수정">수정</button></c:if>
                        </li>
                    </c:forEach>
                </ul>
            </section>
            <section class="group-card" data-panel="rules">
                <div class="group-card-heading"><h2>약속 규칙 리스트</h2>
                    <c:if test="${active}"><button type="button" data-dialog="rule-new">규칙 추가 +</button></c:if>
                </div>
                <c:if test="${empty groupPage.rules}"><p class="group-empty">함께 지킬 첫 번째 약속을 정해 보세요.</p></c:if>
                <ol class="group-rule-list">
                    <c:forEach items="${groupPage.rules}" var="rule" varStatus="loop">
                        <li><span class="group-rule-number"><c:if test="${loop.count < 10}">0</c:if>${loop.count}</span>
                            <div class="item-content"><p class="group-rule-text"><c:out value="${rule.content}"/></p>
                                <p class="backend-muted">동의 <c:out value="${groupPage.agreementCounts[rule.ruleId]}" default="0"/> / ${groupPage.members.size()}명</p>
                            </div>
                            <div class="item-actions">
                                <c:choose>
                                    <c:when test="${groupPage.agreementCounts[rule.ruleId] == groupPage.members.size()}"><span class="group-badge group-badge-agreed">전원 동의</span></c:when>
                                    <c:when test="${groupPage.agreedRuleIds.contains(rule.ruleId)}"><span class="group-badge group-badge-pending">동의 완료</span></c:when>
                                    <c:when test="${active}"><form method="post" action="${pageContext.request.contextPath}/app/group/rule/agree"><%@ include file="fields.jspf" %><input type="hidden" name="ruleId" value="${rule.ruleId}"><button class="text-button">동의하기</button></form></c:when>
                                </c:choose>
                                <c:if test="${active}"><button type="button" class="text-button" data-dialog="rule-${rule.ruleId}" aria-label="규칙 ${loop.count} 수정">수정</button></c:if>
                            </div>
                        </li>
                    </c:forEach>
                </ol>
            </section>
        </div>
        <section class="group-card leave-card" data-panel="leave">
            <div class="group-card-heading"><h2>룸메 퇴실 요청</h2><span class="group-badge">구성원 동의</span></div>
            <p class="backend-muted">나를 제외한 룸메이트 전원이 동의하면 그룹에서 퇴실합니다.</p>
            <c:if test="${active}">
                <button type="button" class="button button-outline" id="open-leave" data-dialog="leave-dialog" ${pendingLeave || groupPage.members.size() < 2 ? 'disabled' : ''}>퇴실 요청하기</button>
                <c:if test="${pendingLeave}"><p class="backend-muted">진행 중인 요청에 대한 응답을 기다리고 있어요.</p></c:if>
                <c:if test="${groupPage.members.size() < 2}"><p class="backend-muted">마지막 구성원의 퇴실은 관리자에게 문의해 주세요.</p></c:if>
            </c:if>
            <c:if test="${empty groupPage.leaveRequests}"><p class="group-empty">진행 중인 퇴실 요청이 없습니다.</p></c:if>
            <c:forEach items="${groupPage.leaveRequests}" var="leave">
                <article class="leave-request">
                    <div class="group-card-heading"><strong><c:out value="${groupPage.memberNames[leave.requesterId]}" default="퇴실한 구성원"/></strong>
                        <span class="group-badge ${leave.status == 'APPROVED' ? 'group-badge-agreed' : 'group-badge-pending'}"><c:choose><c:when test="${leave.status == 'PENDING'}">동의 대기</c:when><c:when test="${leave.status == 'APPROVED'}">퇴실 완료</c:when><c:otherwise>요청 거절</c:otherwise></c:choose></span>
                    </div>
                    <p class="leave-reason"><c:out value="${empty leave.reason ? '작성한 사유가 없습니다.' : leave.reason}"/></p>
                    <ul class="consent-list"><c:forEach items="${groupPage.consents[leave.leaveId]}" var="vote">
                        <li><span><c:out value="${groupPage.memberNames[vote.memberId]}" default="이전 구성원"/></span><span><c:choose>
                            <c:when test="${vote.agreed == 'Y'}">동의</c:when>
                            <c:when test="${vote.agreed == 'N'}">거절</c:when>
                            <c:otherwise>미응답</c:otherwise>
                        </c:choose></span></li>
                    </c:forEach></ul>
                    <c:if test="${active && groupPage.answerableLeaves.contains(leave.leaveId)}"><button type="button" class="button button-outline" data-dialog="consent-${leave.leaveId}">요청 확인하기</button></c:if>
                </article>
            </c:forEach>
        </section>
    </div>
</main>
<c:if test="${active}">
    <%@ include file="edit-dialogs.jspf" %>
    <%@ include file="leave.jspf" %>
</c:if>
<c:if test="${not empty reopenDialog}">
    <div id="form-retry" hidden data-dialog="<c:out value='${reopenDialog}'/>" data-return-url="${pageContext.request.contextPath}/app/group?groupId=${groupPage.group.groupId}"
         data-role-title="<c:out value='${param.roleTitle}'/>" data-assignee-id="<c:out value='${param.assigneeId}'/>"
         data-content="<c:out value='${param.content}'/>" data-reason="<c:out value='${param.reason}'/>"></div>
</c:if>
<script src="${pageContext.request.contextPath}/assets/js/group/backend.js?v=20261124"></script>
</body>
</html>
