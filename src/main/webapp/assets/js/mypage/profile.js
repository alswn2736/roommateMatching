(function () {
    var ctx = document.body.dataset.ctx || '';

    // ===== 공개 범위 토글 =====
    document.querySelectorAll('.infoPublicToggle').forEach(function (box) {
        var text = box.parentElement.querySelector('.infoPublicText');

        // 초기 상태: 서버에서 그려준 글자가 "공개됨"이면 켜짐
        var isOn = text.textContent.trim() === '공개됨';
        var busy = false;

        // 스위치 버튼 생성
        var btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'toggleSwitch';
        btn.setAttribute('role', 'switch');
        box.appendChild(btn);

        function render() {
            btn.setAttribute('aria-checked', isOn);
            text.textContent = isOn ? '공개됨' : '비공개';
            text.classList.toggle('off', !isOn);
        }
        render();

        btn.addEventListener('click', function () {
            if (busy) return;
            busy = true;
            isOn = !isOn;
            render();

            fetch(ctx + '/mypage/profile/visibility', {
                method: 'POST',
                headers: { 'X-Requested-With': 'XMLHttpRequest' },
                body: new URLSearchParams({ field: box.dataset.field, value: isOn ? 'Y' : 'N' })
            })
                .then(function (res) {
                    return res.json().then(function (data) { return { res: res, data: data }; });
                })
                .then(function (r) {
                    if (r.res.status === 401 && r.data.redirect) {
                        location.href = r.data.redirect;
                        return;
                    }
                    if (!r.data.ok) throw new Error(r.data.message);
                })
                .catch(function (err) {
                    // 저장 실패 시 원래 상태로 되돌림
                    isOn = !isOn;
                    render();
                    alert((err && err.message) || '저장에 실패했습니다.');
                })
                .finally(function () {
                    busy = false;
                });
        });
    });

    // ===== 프로필 사진 미리보기 / 검사 =====
    var form = document.querySelector('.profileImgForm');
    if (!form) return;
    var input = form.querySelector('.newImageUpload');
    var preview = form.querySelector('.ProfileImg');
    var originalBg = preview ? preview.style.backgroundImage : '';
    var MAX = 5 * 1024 * 1024;

    function checkFile(file) {
        if (!file) return '업로드할 사진을 선택해주세요.';
        if (!/\.(jpe?g|png)$/i.test(file.name)) return 'JPG, PNG 형식의 파일만 업로드할 수 있습니다.';
        if (file.size > MAX) return '5MB 이하의 파일만 업로드할 수 있습니다.';
        return null;
    }

    input.addEventListener('change', function () {
        var file = input.files[0];
        var error = file ? checkFile(file) : null;
        if (error) {
            alert(error);
            input.value = '';
        }
        if (preview) {
            preview.style.backgroundImage = (file && !error)
                ? 'url("' + URL.createObjectURL(file) + '")'
                : originalBg;
        }
    });

    form.addEventListener('submit', function (e) {
        var error = checkFile(input.files[0]);
        if (error) {
            e.preventDefault();
            alert(error);
        }
    });
})();
