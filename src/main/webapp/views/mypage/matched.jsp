<%@ page contentType="text/html; charset=utf-8" %>

<%@ include file =  "/views/common/head.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/mypage/matched.css" rel="stylesheet" type="text/css">
<title>Matching Information</title>
</head>

<body>
     <%@ include file = "/views/common/header.jsp" %>
     <div class="mainLayout">
        <%@ include file = "/views/mypage/mypage_leftsidebar.jsp" %>
        <div class="contentArea">
            <div class="sectionTitle">
                <span class="title">매칭 정보</span>
                <span class="subtitle">현재 확정된 룸메이트 그룹의 세부 정보 및 지난 기록을 확인하실 수 있습니다.</span>
            </div>
            <div class="activeGroupContainer">
                <div class="groupHeader">
                    <div class="titleArea">
                        <div class="badgeRow">
                            <div class="roomBadge">
                                <span class="roomBadgeText">2인실 기숙사</span>
                            </div>
                            <div class="matchSuccessBadge">
                                <span class="matchSuccessBadgeText">매칭 성공</span>
                            </div>
                        </div>
                        <span>A동 302호 우리들의 따뜻한 단짝방</span>
                    </div>
                    <span class="startDate">시작일: 2026.03.02</span>
                </div>
                <hr style="height: 1px; background: #E7E1DF;">
                <div class="mateGrid">
                    <div class="mateCard">
                        <div class="userMeta">
                            <div class="profileImg" style="background-image: url('${pageContext.request.contextPath}/assets/image/mypage/Avatar.png');"></div>
                            <div class="profileFrame">
                                <div class="frame">
                                    <span class="profileName">김솜솜</span>
                                    <div class="meBadge">
                                        <span>나</span>
                                    </div>
                                </div>
                                <span>컴퓨터공학과 • 20학번</span>
                            </div>
                        </div>
                    </div>
                    <div class="mateCard">
                        <div class="userMeta">
                            <div class="profileImg" style="background-image: url('${pageContext.request.contextPath}/assets/image/mypage/Avatar.png');"></div>
                            <div class="profileFrame">
                                <div class="frame">
                                    <span class="profileName">박솜솜</span>
                                    <div class="meBadge">
                                        <span>나</span>
                                    </div>
                                </div>
                                <span>전자공학과 • 21학번</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="dangerActionRow">
                    <span>* 성격 차이 및 생활습관 불일치 등 부득이한 사유 시 그룹 해체/탈퇴 신청을 보낼 수 있습니다.</span>
                    <button class="btnLeave">
                        <span>룸메이트 탈퇴 요청</span>
                    </button>
                </div>
            </div>
            <div class="pastGroupsSection">
                <span>이전 종료된 그룹 내역</span>
                <div class="pastGroupCard">
                    <div class="leftInfo">
                        <div class="endBadge">
                            <span>종료</span>
                        </div>
                        <span>2025학년도 2학기 희망 기숙사 그룹 (메이트: 정예준)</span>
                    </div>
                    <span class="endDate">종료일: 2025.12.20</span>
                </div>
            </div>
        </div>
     </div>
</body>