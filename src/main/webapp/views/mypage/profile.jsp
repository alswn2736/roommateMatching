<%@ page contentType="text/html; charset=utf-8" %>

    <%@ include file =  "/views/common/head.jsp" %>    
    <link href="${pageContext.request.contextPath}/assets/css/mypage/profile.css" rel="stylesheet" type="text/css">
    <title>profile</title>
    </head>
    <body>
        <%@ include file = "/views/common/header.jsp" %>
        <div class="mainLayout">
            <%@ include file = "/views/mypage/mypage_leftsidebar.jsp" %>
            <div class="profileMain">
                <span class="title">프로필 관리</span>
                <div class="infoWarning">
                    <div class="infoWarningSign">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                            <path d="M9.99998 6.66642V10M9.99998 13.3336H10.0083M18.334 10C18.334 14.6028 14.6027 18.334 9.99998 18.334C5.39724 18.334 1.66599 14.6028 1.66599 10C1.66599 5.39727 5.39724 1.66602 9.99998 1.66602C14.6027 1.66602 18.334 5.39727 18.334 10Z" stroke="#7A273F" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                    </div>
                    <div class="infoWarningText">
                        <span class="infoWarningTextTitle">개인정보 공개 범위 안내</span>
                        <span class="infoWarningTextSubtitle">토글 스위치를 활성화하면 다른 학생들이 해당 정보를 볼 수 있습니다.</span>
                    </div>
                </div>
                <form action="${pageContext.request.contextPath}/views/mypage/update.jsp" method="post" class="profileSetting">
                    <span class="profileSettingTitle">내 기본 정보 및 공개 범위</span>
                    <div class="profileSettingLayout">
                        <div>
                            <div class="infoRow">
                                <span class="infoLabel">이름</span>
                                <span class="infoValue">김솜솜</span>
                            </div>
                            <label class="statusSwitch">
                                <input type="checkbox" name="namePublic" value="Y" checked>
                                <span class="statusText on">공개됨</span>
                                <span class="statusText off">비공개</span>
                                <span class="switchTrack"></span>
                            </label>
                        </div>
                        <div>
                            <div class="infoRow">
                                <span class="infoLabel">학번</span>
                                <span class="infoValue">23학번</span>
                            </div>
                            <label class="statusSwitch">
                                <input type="checkbox" name="studentIdPublic" value="Y">
                                <span class="statusText on">공개됨</span>
                                <span class="statusText off">비공개</span>
                                <span class="switchTrack"></span>
                            </label>
                        </div>
                        <div>
                            <div class="infoRow">
                                <span class="infoLabel">학과</span>
                                <span class="infoValue">컴퓨터공학과</span>
                            </div>
                            <label class="statusSwitch">
                                <input type="checkbox" name="deptPublic" value="Y">
                                <span class="statusText on">공개됨</span>
                                <span class="statusText off">비공개</span>
                                <span class="switchTrack"></span>
                            </label>
                        </div>
                    </div>
                    <div class="btnContent">
                        <button type="button" class="modify">
                            <span>수정하기</span>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </body>

    </html>