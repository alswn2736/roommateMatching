(() => {
    'use strict';
    const root = document.querySelector('.group-main');
    if (!root) return;
    const tabs = root.querySelectorAll('[data-group-tab]');
    tabs.forEach(button => button.addEventListener('click', () => {
        const selected = button.dataset.groupTab;
        tabs.forEach(tab => {
            const active = tab === button;
            tab.classList.toggle('active', active);
            tab.setAttribute('aria-pressed', String(active));
        });
        root.querySelectorAll('[data-group-section]').forEach(section => {
            section.hidden = selected !== 'all' && section.dataset.groupSection !== selected;
        });
        root.querySelector('.group-left').hidden = selected === 'bills';
        root.querySelector('.group-grid').classList.toggle('group-filtered', selected !== 'all');
    }));
    root.querySelectorAll('[data-group-action]').forEach(button => button.addEventListener('click', () => {
        const event = new CustomEvent('roommate:group-action', {
            bubbles: true, cancelable: true,
            detail: { action: button.dataset.groupAction, groupId: root.querySelector('[data-group-id]').value }
        });
        if (button.dispatchEvent(event)) {
            const status = root.querySelector('[data-group-feedback]');
            status.textContent = '이 기능은 서버 연결 후 사용할 수 있습니다. 현재 데이터는 변경되지 않았습니다.';
            status.hidden = false;
        }
    }));
})();
