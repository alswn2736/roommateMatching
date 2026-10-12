(() => {
    'use strict';
    document.querySelectorAll('.roommate-modal').forEach(modal => {
        const form = modal.querySelector('form');
        const action = form.dataset.modalAction;
        const submit = form.querySelector('[type="submit"]');
        const feedback = form.querySelector('.roommate-modal-feedback');
        const confirmation = form.elements.namedItem('confirmation');
        const fileInput = form.elements.namedItem('photo');
        const zoom = form.elements.namedItem('zoom');
        const preview = form.querySelector('[data-photo-preview]');
        const initialPhoto = preview?.getAttribute('src');
        let currentImage = null;
        let objectUrl = null;
        let generation = 0;
        let sending = false;
        const canvas = action === 'photo' ? document.createElement('canvas') : null;
        if (canvas) canvas.width = canvas.height = 512;

        const message = text => {
            feedback.textContent = text;
            feedback.hidden = !text;
        };
        const updateSubmit = () => {
            form.querySelectorAll('input:not([type="hidden"]), textarea').forEach(field => {
                field.disabled = sending;
            });
            submit.disabled = sending || (action === 'withdraw' && confirmation.value !== '탈퇴합니다')
                || (action === 'photo' && !currentImage)
                || (action === 'leave' && !form.elements.namedItem('groupId').value);
        };
        const releaseImage = () => {
            if (objectUrl) URL.revokeObjectURL(objectUrl);
            objectUrl = null;
        };
        const drawPhoto = () => {
            if (!currentImage) return;
            const ctx = canvas.getContext('2d');
            const side = Math.min(currentImage.naturalWidth, currentImage.naturalHeight) / Number(zoom.value);
            ctx.clearRect(0, 0, 512, 512);
            ctx.drawImage(currentImage, (currentImage.naturalWidth - side) / 2,
                (currentImage.naturalHeight - side) / 2, side, side, 0, 0, 512, 512);
            preview.src = canvas.toDataURL('image/png');
            zoom.setAttribute('aria-valuetext', `${Number(zoom.value).toFixed(2)}배`);
        };
        confirmation?.addEventListener('input', () => { message(''); updateSubmit(); });
        zoom?.addEventListener('input', drawPhoto);
        fileInput?.addEventListener('change', async () => {
            const version = ++generation;
            releaseImage();
            currentImage = null;
            preview.src = initialPhoto;
            zoom.value = '1';
            zoom.setAttribute('aria-valuetext', '1배');
            updateSubmit();
            message('');
            const file = fileInput.files[0];
            if (!file) return;
            if (!['image/jpeg', 'image/png'].includes(file.type) || file.size > 5 * 1024 * 1024 || !file.size) {
                message('5MB 이하의 JPG 또는 PNG 파일을 선택해주세요.');
                fileInput.value = '';
                return;
            }
            objectUrl = URL.createObjectURL(file);
            const candidate = new Image();
            candidate.src = objectUrl;
            try {
                await candidate.decode();
                if (version !== generation) return;
                if (candidate.naturalWidth * candidate.naturalHeight > 40000000) {
                    throw new Error('Image dimensions exceeded');
                }
                currentImage = candidate;
                drawPhoto();
            } catch (_) {
                if (version !== generation) return;
                currentImage = null;
                fileInput.value = '';
                message('이미지를 읽을 수 없거나 해상도가 너무 큽니다. 다른 사진을 선택해주세요.');
            } finally {
                if (version === generation) { releaseImage(); updateSubmit(); }
            }
        });
        form.addEventListener('submit', async event => {
            event.preventDefault();
            updateSubmit();
            if (submit.disabled || !form.reportValidity()) return;
            const version = generation;
            const data = new FormData(form);
            sending = true;
            updateSubmit();
            if (action === 'photo') {
                const cropped = await new Promise(resolve => canvas.toBlob(resolve, 'image/png'));
                if (version !== generation) { sending = false; updateSubmit(); return; }
                if (!cropped) {
                    message('사진을 처리하지 못했습니다. 다시 선택해주세요.');
                    sending = false;
                    updateSubmit();
                    return;
                }
                data.set('photo', cropped, 'profile.png');
                data.delete('zoom');
            }
            let finished = false;
            const complete = ({ message: text, success = false } = {}) => {
                if (finished || version !== generation) return;
                finished = true;
                sending = false;
                updateSubmit();
                message(text || (success ? '처리가 완료되었습니다.' : '처리하지 못했습니다. 다시 시도해주세요.'));
            };
            const request = new CustomEvent('roommate:modal-submit', {
                bubbles: true, cancelable: true, detail: { action, formData: data, complete }
            });
            try {
                if (form.dispatchEvent(request)) {
                    complete({ message: '서버 연결 전입니다. 정보는 저장·삭제되지 않았습니다.' });
                }
            } catch (_) {
                complete({ message: '처리 중 오류가 발생했습니다. 다시 시도해주세요.' });
            }
        });
        modal.addEventListener('hide.bs.modal', event => { if (sending) event.preventDefault(); });
        modal.addEventListener('hidden.bs.modal', () => {
            ++generation;
            form.reset();
            message('');
            releaseImage();
            currentImage = null;
            if (preview) preview.src = initialPhoto;
            if (zoom) zoom.setAttribute('aria-valuetext', '1배');
            updateSubmit();
        });
        modal.addEventListener('shown.bs.modal', () => {
            (form.querySelector('textarea, input:not([type="hidden"]), button'))?.focus();
        });
        modal.addEventListener('keydown', event => {
            if (event.key !== 'Tab') return;
            const fields = Array.from(form.querySelectorAll('button:not(:disabled), input:not(:disabled):not([type="hidden"]), textarea:not(:disabled), select:not(:disabled), a[href], [tabindex="0"]'))
                .filter(field => field.getClientRects().length > 0);
            if (!fields.length) { event.preventDefault(); return; }
            const first = fields[0];
            const last = fields[fields.length - 1];
            if (event.shiftKey && document.activeElement === first) {
                event.preventDefault(); last.focus();
            } else if (!event.shiftKey && document.activeElement === last) {
                event.preventDefault(); first.focus();
            }
        });
        updateSubmit();
    });
})();
