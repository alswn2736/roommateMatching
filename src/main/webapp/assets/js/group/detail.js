const groupTabs = document.querySelectorAll('[data-group-tab]');
const groupSections = document.querySelectorAll('[data-group-section]');

groupTabs.forEach(function(button) {
    button.addEventListener('click', function() {
        const selected = button.dataset.groupTab;

        groupTabs.forEach(function(tab) {
            tab.classList.toggle('active', tab === button);
            tab.setAttribute('aria-pressed', String(tab === button));
        });

        groupSections.forEach(function(section) {
            section.hidden = selected !== 'all' && section.dataset.groupSection !== selected;
        });

        document.querySelector('.group-left').hidden = selected === 'bills';
        document.querySelector('.group-grid').classList.toggle('group-filtered', selected !== 'all');
    });
});

document.querySelectorAll('.group-edit-button').forEach(function(button) {
    button.addEventListener('click', function() {
        const feedback = document.getElementById('group-feedback');
        feedback.textContent = '아직 준비 중인 기능입니다.';
        feedback.hidden = false;
    });
});
