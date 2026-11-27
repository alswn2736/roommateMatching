<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- me(로그인 사용자), menu(현재 메뉴)는 각 마이페이지 Command 에서 request 에 담아 준다 --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="mypageLeftsidebar">
    <div class="minProfile">
        <div class="minProfileImg" style="background-image: url('${ctx}<c:out value="${me.profileImgPath}"/>');"></div>
        <div class="nameMajor">
            <div class="minProfileName"><c:out value="${me.myName}" /></div>
            <div class="minProfileClass"><c:out value="${me.myMajor}" /></div>
        </div>
    </div>
    <ul class="menuContainer">
        <li class="menuItem ${menu == 'profile' ? 'active' : ''}">
            <a href="${ctx}/mypage/profile">프로필 관리</a>
        </li>
        <li class="menuItem ${menu == 'applicants' ? 'active' : ''}">
            <a href="${ctx}/mypage/applicants">신청자 관리</a>
        </li>
        <li class="menuItem ${menu == 'matched' ? 'active' : ''}">
            <a href="${ctx}/mypage/matched">매칭 정보</a>
        </li>
        <li class="menuItem ${menu == 'account' ? 'active' : ''}">
            <a href="${ctx}/mypage/account">계정 설정</a>
        </li>
    </ul>
</div>
