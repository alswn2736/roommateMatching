<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- JSP 를 직접 열면(데이터 없음) 컨트롤러를 거치도록 이동 --%>
<c:if test="${empty me}"><c:redirect url="/mypage/applicants" /></c:if>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

    <%@ include file =  "/views/common/head.jsp" %>
    <link href="${pageContext.request.contextPath}/assets/css/mypage/applicants.css" rel="stylesheet" type="text/css">
    <script defer src="${pageContext.request.contextPath}/assets/js/mypage/applicants.js"></script>
    <title>applicants</title>
    </head>
    <body data-ctx="${pageContext.request.contextPath}">
        <%@ include file = "/views/common/header.jsp" %>
        <div class="mainLayout">
            <%@ include file = "/views/mypage/mypage_leftsidebar.jsp" %>
            <div class="contentArea">
                <div class="sectionTitle">
                    <span class="title">신청자 관리</span>
                    <span class="subtitle">내 모집글에 룸메이트를 신청한 학우들의 정보를 확인하고 수락해보세요.</span>
                </div>

                <c:if test="${empty myPosts}">
                    <div class="emptyCard">
                        <span>아직 작성한 모집글이 없습니다.</span>
                        <a class="emptyLink" href="${ctx}/views/feed/feed.jsp">모집글 보러 가기</a>
                    </div>
                </c:if>

                <c:forEach var="mp" items="${myPosts}" varStatus="st">
                    <c:choose>
                        <%-- ===== 모집중인 글 ===== --%>
                        <c:when test="${mp.recruiting}">
                            <details class="recruitingCard" data-post-id="${mp.post.postId}" ${st.first ? 'open' : ''}>
                                <summary class="cardHeader">
                                    <div class="headerLeft">
                                        <div class="headerBadges">
                                            <div class="roomBadge">
                                                <span class="roomBadgeText"><c:out value="${mp.roomLabel}" /></span>
                                            </div>
                                            <div class="recruitingBadge">
                                                <span class="recruitingBadgeText">모집중</span>
                                            </div>
                                        </div>
                                        <span class="headerTitle"><c:out value="${mp.post.title}" /></span>
                                    </div>
                                    <div class="arrowToggle">
                                        <span class="arrowToggleText">신청자 <span class="pendingCount">${mp.pendingCount}</span>명 대기중</span>
                                        <svg class="arrowToggleChevron" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                            <path d="M4 6L8 10L12 6" stroke="#7A273F" stroke-width="2" stroke-linecap="round"/>
                                        </svg>
                                    </div>
                                </summary>

                                <div class="applicantList">
                                    <c:if test="${empty mp.applicants}">
                                        <span class="emptyText">아직 신청한 학우가 없습니다.</span>
                                    </c:if>

                                    <c:forEach var="a" items="${mp.applicants}">
                                        <%-- 채팅 버튼을 누르면 JS 가 applicantCardNone ↔ applicantCardSelected 로 바꿔 줌 --%>
                                        <div class="applicantCardNone" data-apply-id="${a.applyId}">
                                            <div class="applicantInfo">
                                                <div class="profileImg" style="background-image: url('${ctx}<c:out value="${a.profileImgPath}"/>');"></div>
                                                <div class="textDetails">
                                                    <div class="nameHeader">
                                                        <span class="name"><c:out value="${a.displayName}" /></span>
                                                        <span class="bio"><c:out value="${a.bio}" /></span>
                                                    </div>
                                                    <span class="greeting">
                                                        <c:choose>
                                                            <c:when test="${not empty a.introduction}">"<c:out value="${a.introduction}" />"</c:when>
                                                            <c:otherwise>작성한 자기소개가 없습니다.</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>
                                                <div class="actionButtons">
                                                    <button type="button" class="btnChat">
                                                        <div class="messageSquare">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                                                <path d="M14.6672 11.3334C14.6672 11.687 14.5267 12.0261 14.2766 12.2762C14.0265 12.5262 13.6874 12.6667 13.3337 12.6667H4.55169C4.19807 12.6668 3.85896 12.8073 3.60895 13.0574L2.14083 14.5254C2.07463 14.5916 1.99029 14.6367 1.89847 14.6549C1.80665 14.6732 1.71148 14.6638 1.62499 14.628C1.5385 14.5922 1.46458 14.5315 1.41256 14.4537C1.36054 14.3758 1.33278 14.2843 1.33276 14.1907V3.33334C1.33276 2.97972 1.47325 2.64058 1.72332 2.39053C1.97339 2.14048 2.31255 2 2.6662 2H13.3337C13.6874 2 14.0265 2.14048 14.2766 2.39053C14.5267 2.64058 14.6672 2.97972 14.6672 3.33334V11.3334Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
                                                            </svg>
                                                        </div>
                                                        <span class="btnChatText">채팅</span>
                                                    </button>
                                                    <c:choose>
                                                        <c:when test="${a.pending}">
                                                            <button type="button" class="btnDecline">
                                                                <span>거절</span>
                                                            </button>
                                                            <button type="button" class="btnAccept">
                                                                <span>수락하기</span>
                                                            </button>
                                                        </c:when>
                                                        <c:when test="${a.accepted}">
                                                            <div class="acceptedBadge"><span>수락됨</span></div>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="chatSection">
                                                <span>실시간 문의 채팅</span>
                                                <div class="chatHistory"></div>
                                                <div class="chatInputRow">
                                                    <input class="inputWrapper" maxlength="1000" placeholder="메시지를 입력하세요...">
                                                    <button type="button" class="btnSend">
                                                        <span>전송</span>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </details>
                        </c:when>

                        <%-- ===== 모집 완료된 글 ===== --%>
                        <c:otherwise>
                            <div class="recruitedCard">
                                <div class="cardHeader">
                                    <div class="headerLeft">
                                        <div class="headerBadges">
                                            <div class="roomBadge">
                                                <span class="roomBadgeText"><c:out value="${mp.roomLabel}" /></span>
                                            </div>
                                            <div class="recruitedBadge">
                                                <span class="recruitedBadgeText">모집완료</span>
                                            </div>
                                        </div>
                                        <span class="recruitedheaderTitle"><c:out value="${mp.post.title}" /></span>
                                    </div>
                                    <a class="arrowToggle" href="${ctx}/mypage/matched">
                                        <span class="arrowTogglerRecruitedText">신청 마감</span>
                                        <svg class="arrowToggleChevron" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                            <path d="M6 12L10 8L6 4" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
                                        </svg>
                                    </a>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
