(function () {
    // ===== 비밀번호 변경: 제출 전 확인 =====
    var pwForm = document.querySelector('form[name="modifyAccount"]');
    if (pwForm) {
        pwForm.addEventListener('submit', function (e) {
            var pw = pwForm.newPw.value;
            var confirmPw = pwForm.confirmNewPw.value;
            if (pw.length < 8) {
                e.preventDefault();
                alert('비밀번호는 8자리 이상이어야 합니다.');
                return;
            }
            if (pw !== confirmPw) {
                e.preventDefault();
                alert('비밀번호 확인이 일치하지 않습니다.');
            }
        });
    }

    // ===== 서비스 탈퇴: '탈퇴합니다' 입력 확인 =====
    var deleteForm = document.querySelector('.deleteActionRow');
    if (deleteForm) {
        deleteForm.addEventListener('submit', function (e) {
            var typed = prompt("정말 탈퇴하시겠어요? 모든 정보가 영구 삭제되며 복구할 수 없습니다.\n확인을 위해 '탈퇴합니다'를 입력해주세요.");
            if (typed === null) {
                e.preventDefault();
                return;
            }
            if (typed.trim() !== '탈퇴합니다') {
                e.preventDefault();
                alert("'탈퇴합니다'를 정확히 입력해야 탈퇴할 수 있습니다.");
                return;
            }
            deleteForm.confirmation.value = typed.trim();
        });
    }
})();
