<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- JSP 를 직접 열면(데이터 없음) 컨트롤러를 거치도록 이동 --%>
<c:if test="${empty me}"><c:redirect url="/mypage/matched" /></c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%@ include file =  "/views/common/head.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/mypage/matched.css" rel="stylesheet" type="text/css">
<script defer src="${pageContext.request.contextPath}/assets/js/mypage/matched.js"></script>
<title>Matching Information</title>
</head>

<body data-ctx="${pageContext.request.contextPath}">
     <%@ include file = "/views/common/header.jsp" %>
     <div class="mainLayout">
        <%@ include file = "/views/mypage/mypage_leftsidebar.jsp" %>
        <div class="contentArea">
            <div class="sectionTitle">
                <span class="title">매칭 정보</span>
                <span class="subtitle">현재 확정된 룸메이트 그룹의 세부 정보 및 지난 기록을 확인하실 수 있습니다.</span>
            </div>

            <c:if test="${empty activeGroups}">
                <div class="emptyCard">
                    <span>아직 확정된 룸메이트 그룹이 없습니다.</span>
                    <a class="emptyLink" href="${ctx}/views/feed/feed.jsp">룸메이트 찾으러 가기</a>
                </div>
            </c:if>

            <c:forEach var="g" items="${activeGroups}">
                <div class="activeGroupContainer">
                    <div class="groupHeader">
                        <div class="titleArea">
                            <div class="badgeRow">
                                <div class="roomBadge">
                                    <span class="roomBadgeText"><c:out value="${g.roomLabel}" /></span>
                                </div>
                                <div class="matchSuccessBadge">
                                    <span class="matchSuccessBadgeText">매칭 성공</span>
                                </div>
                            </div>
                            <span>우리들의 룸메이트 그룹 (${g.members.size()}명)</span>
                        </div>
                        <span class="startDate">기간: <c:out value="${g.termLabel}" /></span>
                    </div>
                    <hr style="height: 1px; background: #E7E1DF;">
                    <div class="mateGrid">
                        <c:forEach var="p" items="${g.members}">
                            <c:set var="isMe" value="${p.studentId == me.studentId}" />
                            <div class="mateCard">
                                <div class="userMeta">
                                    <div class="profileImg" style="background-image: url('${ctx}<c:out value="${p.profileImgPath}"/>');"></div>
                                    <div class="profileFrame">
                                        <div class="frame">
                                            <span class="profileName"><c:out value="${isMe ? me.myName : p.displayName}" /></span>
                                            <c:if test="${isMe}">
                                                <div class="meBadge">
                                                    <span>나</span>
                                                </div>
                                            </c:if>
                                        </div>
                                        <span>
                                            <c:choose>
                                                <c:when test="${isMe}"><c:out value="${me.myMajor} • ${me.yearLabel}" /></c:when>
                                                <c:otherwise><c:out value="${p.bio}" /></c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="dangerActionRow">
                        <a href="${ctx}/app/group?groupId=${g.group.groupId}#leave">그룹 상세 및 퇴실 응답</a>
                        <span>* 성격 차이 및 생활습관 불일치 등 부득이한 사유 시 그룹 해체/탈퇴 신청을 보낼 수 있습니다.</span>
                        <c:choose>
                            <c:when test="${g.leavePending}">
                                <button type="button" class="btnLeave" disabled>
                                    <span>탈퇴 요청 대기중</span>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btnLeave" data-group-id="${g.group.groupId}">
                                    <span>룸메이트 탈퇴 요청</span>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>

            <div class="pastGroupsSection">
                <span>이전 종료된 그룹 내역</span>
                <c:if test="${empty pastGroups}">
                    <span class="emptyText">종료된 그룹이 없습니다.</span>
                </c:if>
                <c:forEach var="g" items="${pastGroups}">
                    <div class="pastGroupCard">
                        <div class="leftInfo">
                            <div class="endBadge">
                                <span>종료</span>
                            </div>
                            <span><c:out value="${g.termLabel} ${g.roomLabel} 그룹 (메이트: ${g.mateNames})" /></span>
                        </div>
                        <span class="endDate"><c:out value="${g.termLabel}" /></span>
                    </div>
                </c:forEach>
            </div>
        </div>
     </div>
</body>
</html>
