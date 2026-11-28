<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="ko"><head><meta charset="UTF-8"><title>그룹 요청 안내</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/group/backend.css"></head>
<body><main class="group-error"><h1>요청을 처리하지 못했습니다</h1><p><c:out value="${groupError}"/></p>
<a href="${pageContext.request.contextPath}/app/matched">내 그룹 목록</a> · <a href="${pageContext.request.contextPath}/views/auth/login.jsp">로그인 화면</a></main></body></html>
