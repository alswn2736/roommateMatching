<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="ko"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>내 매칭 그룹</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common/header.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/group/detail.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/group/backend.css?v=20261124"></head>
<body class="group-page"><jsp:include page="/views/common/header.jsp"/><main class="group-main"><h1>내 매칭 그룹</h1>
<c:if test="${not empty groupMessage}"><p role="status"><c:out value="${groupMessage}"/></p></c:if>
<c:if test="${empty groupList}"><p>아직 소속된 그룹이 없습니다. 매칭이 완료되면 이곳에서 확인할 수 있습니다.</p></c:if>
<div class="group-list"><c:forEach items="${groupList}" var="group">
<c:url value="/app/group" var="detailUrl"><c:param name="groupId" value="${group.groupId}"/></c:url>
<article class="group-card"><h2><c:out value="${group.academicYear}"/>년 <c:out value="${group.semester}"/>학기</h2>
<p><c:out value="${group.capacity}"/>인 그룹 · <c:out value="${group.status == 'ACTIVE' ? '활동 중' : '종료'}"/></p>
<a href="<c:out value='${detailUrl}'/>">그룹 상세 보기</a></article>
</c:forEach></div></main></body></html>
