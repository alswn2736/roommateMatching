<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="modal fade roommate-modal roommate-leave" id="roommate-leave" tabindex="-1" aria-labelledby="roommate-leave-title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <form class="roommate-modal-form" data-modal-action="leave" novalidate>
            <div class="roommate-modal-heading"><h2 id="roommate-leave-title">룸메 탈퇴 요청</h2></div>

<div class="roommate-leave-warning"><img src="${pageContext.request.contextPath}/assets/image/modal/alert.svg" width="16" height="16" alt=""><p>나를 제외한 룸메이트 전원이 동의해야 진행됩니다.</p></div>
<div class="roommate-leave-group"><span aria-hidden="true"></span><div><strong><c:out value="${requestScope.groupView.name}" default="그룹 정보 없음" /></strong><p>매칭일: <c:out value="${requestScope.groupView.matchedDate}" default="-" /></p></div></div>
<div><label for="roommate-leave-reason">탈퇴 사유 (선택)</label><textarea class="form-control" id="roommate-leave-reason" name="reason" maxlength="1000" placeholder="사유를 입력해주세요."></textarea></div>
<input type="hidden" name="groupId" value="<c:out value='${requestScope.groupView.id}' />">

            <input type="hidden" name="csrfToken" value="<c:out value='${requestScope.csrfToken}' />">
            <p class="roommate-modal-feedback" role="status" hidden></p>
            <div class="roommate-modal-actions">
                <button class="btn roommate-modal-cancel" type="button" data-bs-dismiss="modal">취소</button>
                <button class="btn roommate-modal-submit roommate-modal-danger" type="submit" disabled>탈퇴 요청 보내기</button>
            </div>
        </form>
    </div></div>
</div>
