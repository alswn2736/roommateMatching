<%@ page contentType="text/html; charset=utf-8" %>

<%@ include file =  "/views/common/head.jsp" %>
<link href="${pageContext.request.contextPath}/assets/css/mypage/account.css" rel="stylesheet" type="text/css">
<title>Account Information</title>
</head>

<body>
    <%@ include file = "/views/common/header.jsp" %>
    <div class="mainLayout">
        <%@ include file = "/views/mypage/mypage_leftsidebar.jsp" %>

        <div class="contentArea">
            <div class="sectionTitle">
                <span class="title">계정 설정</span>
                <span class="subtitle">본인 인증 정보 및 계정 보안 설정을 수정하고 사이트 상태를 관리합니다.</span>
            </div>
            <div class="accountCard">
                <span>계정 기본 정보 및 비밀번호 변경</span>
                <form class="formInputs" name="modifyAccount" method="post">
                    <div class="fieldRow">
                        <span>학교 인증 이메일 (변경 불가)</span>
                        <input class="studentMail" placeholder="student@university.ac.kr" readonly>
                    </div>
                    <div class="passwordInputs">
                        <div class="newPassword">
                            <span>새 비밀번호</span>
                            <input class="newPwInput" type="password" name="newPw">
                        </div>
                        <div class="newPassword">
                            <span>비밀번호 확인</span>
                            <input class="newPwInput" type="password" name="confirmNewPw">
                        </div>
                    </div>
                    <div class="saveRow">
                        <button class="btnSavePassword">
                            <span>비밀번호 변경 적용</span>
                        </button>
                    </div>
                </form>
            </div>
            <div class="logoutCard">
                <div class="infoText">
                    <span>로그아웃</span>
                </div>
                <button class="btnLogout">
                    <span>로그아웃</span>
                </button>
            </div>
            <div class="deleteZoneCard">
                <div class="deleteHeader">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                    <g clip-path="url(#clip0_9_690)">
                        <path d="M9.99998 6.66642V10M9.99998 13.3336H10.0083M18.334 10C18.334 14.6028 14.6027 18.334 9.99998 18.334C5.39724 18.334 1.66599 14.6028 1.66599 10C1.66599 5.39727 5.39724 1.66602 9.99998 1.66602C14.6027 1.66602 18.334 5.39727 18.334 10Z" stroke-width="2" stroke-linecap="round" stroke="#DC2626"/>
                    </g>
                    <defs>
                        <clipPath id="clip0_9_690">
                        <rect width="20" height="20" fill="white"/>
                        </clipPath>
                    </defs>
                    </svg>
                    <span>서비스 탈퇴</span>
                </div>
                <div class="deleteDescription">
                    <span class="descriptionTitle">
                        계정을 삭제하면 아래 정보가 완전 영구 소멸됩니다:
                    </span>
                    <span class="description">• 프로필 정보 </span>
                    <span class="description">• 과거 진행 중이거나 매칭되었던 다른 룸메이트 학우들과의 대화 내역 전체</span>
                </div>
                <div class="deleteActionRow">
                    <button class="btnDelete">
                        <span>룸메이트 서비스 탈퇴하기</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>