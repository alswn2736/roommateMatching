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
                <div class="profileSetting">
                    <form>
                        <div class="updateProfileImg">
                            <div class="updateProfileHeader">
                                <div class="updateProfileHeaderTitle">
                                    <span class="profileTitle">프로필 사진 변경</span>
                                    <span class="profileSubtitle">JPG, PNG 형식의 5MB 이하 파일만 업로드할 수 있습니다.</span>
                                </div>
                                <button class="saveProfileImgBtn">
                                    <span>사진 저장</span>
                                </button>
                            </div>
                            <div class="updateProfileImgContent">
                                <div class="proviewArea">
                                    <div class="preview">
                                        <div class="ProfileImg" style="background-image: url('${pageContext.request.contextPath}/assets/image/mypage/profile.jpg');"></div>
                                    </div>
                                    <span>적용될 이미지 비율</span>
                                </div>
                                <div class="editProfileArea">
                                    <input type="file" class="newImageUpload">
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>

    </html>