<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var="roommatePhotoUrl" value="/assets/image/mypage/Avatar.png" />
<c:choose>
    <c:when test="${requestScope.groupPreview}"><c:url var="roommatePhotoUrl" value="/assets/image/modal/photo-sample.png" /></c:when>
    <c:when test="${not empty requestScope.profilePhotoPath}"><c:url var="roommatePhotoUrl" value="${requestScope.profilePhotoPath}" /></c:when>
</c:choose>
<div class="modal fade roommate-modal roommate-photo" id="roommate-photo" tabindex="-1" aria-labelledby="roommate-photo-title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <form class="roommate-modal-form" data-modal-action="photo" novalidate>
            <div class="roommate-modal-heading"><h2 id="roommate-photo-title">프로필 사진 변경</h2><p>JPG, PNG 형식의 5MB 이하 파일만 업로드할 수 있습니다.</p></div>

<div class="roommate-photo-editor">
    <figure><div class="roommate-photo-crop"><img data-photo-preview src="<c:out value='${roommatePhotoUrl}' />" alt="적용될 프로필 사진"></div><figcaption>적용될 아바타 비율</figcaption></figure>
    <div class="roommate-photo-controls"><label class="roommate-photo-upload" for="roommate-photo-file"><img src="${pageContext.request.contextPath}/assets/image/modal/camera.svg" width="20" height="20" alt=""><span>새 이미지 업로드</span><input class="visually-hidden" type="file" id="roommate-photo-file" name="photo" accept="image/jpeg,image/png" required></label>
        <div><label for="roommate-photo-zoom">크기 조절 (줌)</label><div class="roommate-photo-slider"><span aria-hidden="true">-</span><input type="range" id="roommate-photo-zoom" name="zoom" min="1" max="3" step="0.01" value="1" aria-valuetext="1배"><span aria-hidden="true">+</span></div></div>
    </div>
</div>

            <input type="hidden" name="csrfToken" value="<c:out value='${requestScope.csrfToken}' />">
            <p class="roommate-modal-feedback" role="status" hidden></p>
            <div class="roommate-modal-actions">
                <button class="btn roommate-modal-cancel" type="button" data-bs-dismiss="modal">취소</button>
                <button class="btn roommate-modal-submit " type="submit" disabled>사진 저장</button>
            </div>
        </form>
    </div></div>
</div>
