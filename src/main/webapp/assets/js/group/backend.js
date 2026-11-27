const tabs = document.querySelectorAll('[data-section]');
const panels = document.querySelectorAll('[data-panel]');

function selectSection(section) {
    tabs.forEach(function(tab) {
        const selected = tab.dataset.section === section;
        tab.classList.toggle('active', selected);
        tab.setAttribute('aria-pressed', String(selected));
    });
    panels.forEach(function(panel) {
        panel.hidden = section !== 'all' && panel.dataset.panel !== section;
    });
    document.querySelector('.group-left').hidden = section === 'leave';
    document.querySelector('.group-grid').classList.toggle('is-filtered', section !== 'all');
}
tabs.forEach(function(tab) {
    tab.addEventListener('click', function() {
        selectSection(tab.dataset.section);
        history.replaceState(null, '', '#' + tab.dataset.section);
    });
});
const section = location.hash.substring(1);
if (['roles', 'rules', 'leave'].includes(section)) selectSection(section);

let opener;
function openDialog(dialog, button) {
    if (!dialog || dialog.open) return;
    opener = button;
    dialog.showModal();
    document.body.classList.add('has-group-dialog');
}
document.querySelectorAll('button[data-dialog]').forEach(function(button) {
    button.addEventListener('click', function() {
        openDialog(document.getElementById(button.dataset.dialog), button);
    });
});
document.querySelectorAll('.group-dialog').forEach(function(dialog) {
    dialog.querySelectorAll('[data-close]').forEach(function(button) {
        button.addEventListener('click', function() { dialog.close(); });
    });
    dialog.addEventListener('close', function() {
        document.body.classList.remove('has-group-dialog');
        if (opener) opener.focus();
    });
});
const reason = document.getElementById('leave-reason');
function countReason() {
    if (reason) document.getElementById('reason-count').textContent = reason.value.length;
}
if (reason) reason.addEventListener('input', countReason);

const retry = document.getElementById('form-retry');
if (retry) {
    history.replaceState(null, '', retry.dataset.returnUrl);
    const dialog = document.getElementById(retry.dataset.dialog);
    if (dialog) {
        ['roleTitle', 'assigneeId', 'content', 'reason'].forEach(function(name) {
            const input = dialog.querySelector('[name="' + name + '"]');
            if (input) input.value = retry.dataset[name] || '';
        });
        const alert = document.querySelector('.form-error');
        if (alert) dialog.insertBefore(alert, dialog.querySelector('form'));
        openDialog(dialog, document.querySelector('button[data-dialog="' + dialog.id + '"]'));
        countReason();
    }
}
document.querySelectorAll('form[method="post"]').forEach(function(form) {
    form.addEventListener('submit', function(event) {
        if (form.dataset.submitting) {
            event.preventDefault();
            return;
        }
        form.dataset.submitting = 'true';
        form.setAttribute('aria-busy', 'true');
    });
});
window.addEventListener('pageshow', function() {
    document.querySelectorAll('form[data-submitting]').forEach(function(form) {
        delete form.dataset.submitting;
        form.removeAttribute('aria-busy');
    });
});
