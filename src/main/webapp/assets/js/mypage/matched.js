(function () {
    var ctx = document.body.dataset.ctx || '';

    document.querySelectorAll('.btnLeave[data-group-id]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            if (!confirm('룸메이트 탈퇴 요청을 보낼까요?\n나를 제외한 룸메이트 전원이 동의해야 진행됩니다.')) return;

            var reason = prompt('탈퇴 사유를 입력해주세요. (선택, 300자 이내)', '');
            if (reason === null) return; // 취소

            btn.disabled = true;
            fetch(ctx + '/mypage/matched/leave', {
                method: 'POST',
                headers: { 'X-Requested-With': 'XMLHttpRequest' },
                body: new URLSearchParams({ groupId: btn.dataset.groupId, reason: reason })
            })
                .then(function (res) {
                    return res.json().then(function (data) {
                        if (res.status === 401 && data.redirect) {
                            location.href = data.redirect;
                            return;
                        }
                        alert(data.message);
                        if (data.ok) {
                            location.reload();
                        } else {
                            btn.disabled = false;
                        }
                    });
                })
                .catch(function () {
                    alert('요청에 실패했습니다. 잠시 후 다시 시도해주세요.');
                    btn.disabled = false;
                });
        });
    });
})();
