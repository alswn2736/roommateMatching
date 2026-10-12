document.querySelectorAll('.roommate-modal').forEach(function(modal) {
    const form = modal.querySelector('form');
    const submitButton = form.querySelector('[type="submit"]');
    const feedback = form.querySelector('.roommate-modal-feedback');
    const confirmation = form.querySelector('[name="confirmation"]');

    if (form.id === 'leaveForm') {
        submitButton.disabled = false;
    }

    if (confirmation) {
        confirmation.addEventListener('input', function() {
            submitButton.disabled = confirmation.value !== '탈퇴합니다';
            feedback.hidden = true;
        });
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        if (submitButton.disabled || !form.reportValidity()) return;
        feedback.textContent = '아직 준비 중인 기능입니다.';
        feedback.hidden = false;
    });

    modal.addEventListener('hidden.bs.modal', function() {
        form.reset();
        feedback.hidden = true;
        submitButton.disabled = form.id !== 'leaveForm';
    });

    modal.addEventListener('shown.bs.modal', function() {
        form.querySelector('textarea, input, button').focus();
    });

    modal.addEventListener('keydown', function(event) {
        if (event.key !== 'Tab') return;
        const fields = form.querySelectorAll('input:not(:disabled), textarea, button:not(:disabled)');
        const first = fields[0];
        const last = fields[fields.length - 1];

        if (event.shiftKey && document.activeElement === first) {
            event.preventDefault();
            last.focus();
        } else if (!event.shiftKey && document.activeElement === last) {
            event.preventDefault();
            first.focus();
        }
    });
});
