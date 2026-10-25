<%@ page contentType="text/html; charset=utf-8" %>
<div class="mypageLeftsidebar">
    <div class="minProfile">
        <div class="minProfileImg" style="background-image: url('${pageContext.request.contextPath}/assets/image/mypage/Avatar.png');"></div>
        <div class="nameMajor">
            <div class="minProfileName">김솜솜</div>
            <div class="minProfileClass">컴퓨터공학과</div>
        </div>
    </div>
    <ul class="menuContainer">
        <li class="menuItem">
            <a href="${pageContext.request.contextPath}/views/mypage/profile.jsp">프로필 관리</a>
        </li>
        <li class="menuItem">
            <a href="${pageContext.request.contextPath}/views/mypage/applicants.jsp">신청자 관리</a>
        </li>
        <li class="menuItem">
            <a href="${pageContext.request.contextPath}/views/mypage/matched.jsp">매칭 정보</a>
        </li>
        <li class="menuItem">
            <a href="${pageContext.request.contextPath}/views/mypage/account.jsp">계정 설정</a>
        </li>
    </ul>
</div>