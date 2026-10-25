<%@ page contentType="text/html; charset=utf-8" %>

    <%@ include file =  "/views/common/head.jsp" %>
    <link href="${pageContext.request.contextPath}/assets/css/mypage/applicants.css" rel="stylesheet" type="text/css">
    <title>applicants</title>  
    </head>
    <body>
        <%@ include file = "/views/common/header.jsp" %>
        <div class="mainLayout">
            <%@ include file = "/views/mypage/mypage_leftsidebar.jsp" %>
            <div class="contentArea">
                <div class="sectionTitle">
                    <span class="title">신청자 관리</span>
                    <span class="subtitle">내 모집글에 룸메이트를 신청한 학우들의 정보를 확인하고 수락해보세요.</span>
                </div>
                <details class="recruitingCard" open>
                    <summary class="cardHeader">
                        <div class="headerLeft">
                            <div class="headerBadges">
                                <div class="roomBadge">
                                    <span class="roomBadgeText">2인실 희망</span>
                                </div>
                                <div class="recruitingBadge">
                                    <span class="recruitingBadgeText">모집중</span>
                                </div>
                            </div>
                            <span class="headerTitle">새 학기 기숙사 2인실 같이 쓸 동갑내기 컴공 구합니다!</span>
                        </div>
                        <div class="arrowToggle">
                            <span class="arrowToggleText">신청자 2명 대기중</span>
                            <svg class="arrowToggleChevron" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path d="M4 6L8 10L12 6" stroke="#7A273F" stroke-width="2" stroke-linecap="round"/>
                            </svg>
                        </div>
                    </summary>

                    <div class="applicantList">
                        <div class="applicantCardSelected">
                            <div class="applicantInfo">
                                <div class="profileImg" style="background-image: url('${pageContext.request.contextPath}/assets/image/mypage/Avatar.png');"></div>
                                <div class="textDetails">
                                    <div class="nameHeader">
                                        <span class="name">
                                            이솜솜
                                        </span>
                                        <span class="bio">
                                            유아교육과 • 23학번 (23세)
                                        </span>
                                    </div>
                                    <span class="greeting">
                                        "안녕하세요! 아침형이고 방에서 시끄럽게 안 하는 편이에요. 공부 패턴 잘 맞았으면 좋겠습니다!"
                                    </span>
                                </div>
                                <div class="actionButtons">
                                    <button class="btnChat">
                                        <div class="messageSquare">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                                <g clip-path="url(#clip0_9_886)">
                                                    <path d="M14.6672 11.3334C14.6672 11.687 14.5267 12.0261 14.2766 12.2762C14.0265 12.5262 13.6874 12.6667 13.3337 12.6667H4.55169C4.19807 12.6668 3.85896 12.8073 3.60895 13.0574L2.14083 14.5254C2.07463 14.5916 1.99029 14.6367 1.89847 14.6549C1.80665 14.6732 1.71148 14.6638 1.62499 14.628C1.5385 14.5922 1.46458 14.5315 1.41256 14.4537C1.36054 14.3758 1.33278 14.2843 1.33276 14.1907V3.33334C1.33276 2.97972 1.47325 2.64058 1.72332 2.39053C1.97339 2.14048 2.31255 2 2.6662 2H13.3337C13.6874 2 14.0265 2.14048 14.2766 2.39053C14.5267 2.64058 14.6672 2.97972 14.6672 3.33334V11.3334Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
                                                </g>
                                                <defs>
                                                    <clipPath id="clip0_9_886">
                                                    <rect width="16" height="16" fill="white"/>
                                                    </clipPath>
                                                </defs>
                                            </svg>
                                        </div>
                                        <span>
                                            채팅중
                                        </span>
                                    </button>
                                    <button class="btnDecline">
                                        <span>
                                            거절
                                        </span>
                                    </button>
                                    <button class="btnAccept">
                                        <span>수락하기</span>
                                    </button>
                                </div>
                            </div>
                            <div class="chatSection">
                                <span>실시간 문의 채팅</span>
                                <div class="chatHistory">
                                    <div class="revceived">
                                        <div class="receivedBubble">
                                            <span>기숙사 에어컨 조절은 어떻게 조율하고 싶으신가요?</span>
                                        </div>
                                        <span class="chatTime">오후 2:15</span>
                                    </div>

                                    <div class="sent">
                                        <span class="chatTime">오후 2:15</span>
                                        <div class="sentBubble">
                                            <span>저는 추위를 좀 타는 편이라 밤에는 적정 온도로 맞춰서 타이머 맞춰두는 편이에요!</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="chatInputRow">
                                    <input class="inputWrapper" placeholder="메시지를 입력하세요...">
                                    <button class="btnSend">
                                        <span>전송</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="applicantCardNone">
                            <div class="profileImg" style="background-image: url('${pageContext.request.contextPath}/assets/image/mypage/Avatar.png');"></div>
                            <div class="textDetails">
                                <div class="nameHeader">
                                    <span class="name">최솜솜</span>
                                    <span class="bio">컴퓨터공학과 • 22학번 (22세)</span>
                                </div>
                                <span class="greeting">같이 코딩 야작하면서 밤새실 분 환영해요! 주말에는 본가 내려갑니다.</span>
                            </div>
                            <div class="actionButtons">
                                    <button class="btnChat">
                                        <div class="messageSquare">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                                <g clip-path="url(#clip0_9_886)">
                                                    <path d="M14.6672 11.3334C14.6672 11.687 14.5267 12.0261 14.2766 12.2762C14.0265 12.5262 13.6874 12.6667 13.3337 12.6667H4.55169C4.19807 12.6668 3.85896 12.8073 3.60895 13.0574L2.14083 14.5254C2.07463 14.5916 1.99029 14.6367 1.89847 14.6549C1.80665 14.6732 1.71148 14.6638 1.62499 14.628C1.5385 14.5922 1.46458 14.5315 1.41256 14.4537C1.36054 14.3758 1.33278 14.2843 1.33276 14.1907V3.33334C1.33276 2.97972 1.47325 2.64058 1.72332 2.39053C1.97339 2.14048 2.31255 2 2.6662 2H13.3337C13.6874 2 14.0265 2.14048 14.2766 2.39053C14.5267 2.64058 14.6672 2.97972 14.6672 3.33334V11.3334Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
                                                </g>
                                                <defs>
                                                    <clipPath id="clip0_9_886">
                                                    <rect width="16" height="16" fill="white"/>
                                                    </clipPath>
                                                </defs>
                                            </svg>
                                        </div>
                                        <span>
                                            채팅중      <!--수정 필요-->
                                        </span>
                                    </button>
                                    <button class="btnDecline">
                                        <span>
                                            거절
                                        </span>
                                    </button>
                                    <button class="btnAccept">
                                        <span>수락하기</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                </details>
                <div class="recruitedCard">
                    <div class="cardHeader">
                        <div class="headerLeft">
                            <div class="headerBadges">
                                <div class="roomBadge">
                                    <span class="roomBadgeText">3인실 희망</span>
                                </div>
                                <div class="recruitedBadge">
                                    <span class="recruitedBadgeText">모집완료</span>
                                </div>
                            </div>
                            <span class="recruitedheaderTitle">새 학기 조용한 기숙사 룸메이트 모집합니다.</span>
                        </div>
                        <div class="arrowToggle"> <!-- 수정 필요-->
                            <span class="arrowTogglerRecruitedText">신청 마감</span>
                            <svg class="arrowToggleChevron" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path d="M6 12L10 8L6 4" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
