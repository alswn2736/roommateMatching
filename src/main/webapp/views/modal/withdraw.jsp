<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="modal fade roommate-modal roommate-withdraw" id="roommate-withdraw" tabindex="-1" aria-labelledby="roommate-withdraw-title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form id="withdrawForm" class="roommate-modal-form">
                <div class="roommate-modal-heading">
                    <h2 id="roommate-withdraw-title">사이트 탈퇴</h2>
                    <p>탈퇴 시 회원 정보와 아래 데이터가 영구적으로 자동 삭제되며 절대 복구할 수 없습니다.</p>
                </div>

                <div class="roommate-withdraw-warning">
                    <strong>즉시 삭제되는 항목</strong>
                    <ul>
                        <li>내 프로필 정보 및 사진</li>
                        <li>작성한 모든 모집글 및 댓글</li>
                        <li>보냈거나 받은 신청 내역</li>
                        <li>매칭 진행 중인 채팅 및 기록</li>
                    </ul>
                </div>
                <div>
                    <label for="roommate-withdraw-confirmation">확인을 위해 <strong>'탈퇴합니다'</strong>를 입력해주세요</label>
                    <input class="form-control" id="roommate-withdraw-confirmation" name="confirmation" type="text" autocomplete="off" required>
                </div>
                <p class="roommate-modal-feedback" role="status" hidden></p>
                <div class="roommate-modal-actions">
                    <button class="btn roommate-modal-cancel" type="button" data-bs-dismiss="modal">취소</button>
                    <button class="btn roommate-modal-submit roommate-modal-danger" type="submit" disabled>탈퇴하기</button>
                </div>
            </form>
        </div>
    </div>
</div>
