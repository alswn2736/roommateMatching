const photoForm = document.getElementById('photoForm');

if (photoForm) {
    const photoInput = document.getElementById('roommate-photo-file');
    const photoZoom = document.getElementById('roommate-photo-zoom');
    const photoPreview = document.getElementById('photoPreview');
    const photoButton = photoForm.querySelector('[type="submit"]');
    const photoFeedback = photoForm.querySelector('.roommate-modal-feedback');
    const defaultPhoto = photoPreview.src;
    const canvas = document.createElement('canvas');
    canvas.width = 512;
    canvas.height = 512;
    let selectedPhoto = null;

    function drawPhoto() {
        if (!selectedPhoto) return;
        const size = Math.min(selectedPhoto.width, selectedPhoto.height) / Number(photoZoom.value);
        const x = (selectedPhoto.width - size) / 2;
        const y = (selectedPhoto.height - size) / 2;
        const context = canvas.getContext('2d');
        context.clearRect(0, 0, 512, 512);
        context.drawImage(selectedPhoto, x, y, size, size, 0, 0, 512, 512);
        photoPreview.src = canvas.toDataURL('image/png');
        photoZoom.setAttribute('aria-valuetext', photoZoom.value + '배');
    }

    function showPhotoError(message) {
        photoInput.value = '';
        photoFeedback.textContent = message;
        photoFeedback.hidden = false;
    }

    photoInput.addEventListener('change', function() {
        const file = photoInput.files[0];
        selectedPhoto = null;
        photoButton.disabled = true;
        photoPreview.src = defaultPhoto;
        photoZoom.value = '1';
        photoZoom.setAttribute('aria-valuetext', '1배');
        photoFeedback.hidden = true;
        if (!file) return;

        if (!['image/jpeg', 'image/png'].includes(file.type) || file.size === 0 || file.size > 5 * 1024 * 1024) {
            showPhotoError('5MB 이하의 JPG 또는 PNG 파일을 선택해주세요.');
            return;
        }

        const reader = new FileReader();
        reader.onload = function() {
            if (photoInput.files[0] !== file) return;
            const image = new Image();
            image.onload = function() {
                if (photoInput.files[0] !== file) return;
                if (image.width * image.height > 40000000) {
                    showPhotoError('사진 해상도가 너무 큽니다. 다른 사진을 선택해주세요.');
                    return;
                }
                selectedPhoto = image;
                drawPhoto();
                photoButton.disabled = false;
            };
            image.onerror = function() {
                if (photoInput.files[0] === file) showPhotoError('이미지를 읽을 수 없습니다.');
            };
            image.src = reader.result;
        };
        reader.onerror = function() {
            if (photoInput.files[0] === file) showPhotoError('파일을 읽을 수 없습니다.');
        };
        reader.readAsDataURL(file);
    });

    photoZoom.addEventListener('input', drawPhoto);

    document.getElementById('roommate-photo').addEventListener('hidden.bs.modal', function() {
        selectedPhoto = null;
        photoPreview.src = defaultPhoto;
        photoZoom.setAttribute('aria-valuetext', '1배');
    });
}
