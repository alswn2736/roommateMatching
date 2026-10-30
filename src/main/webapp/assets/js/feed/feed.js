document.addEventListener("DOMContentLoaded", function () {

    const postList = document.getElementById("postList");
    const keywordSearch = document.getElementById("keywordSearch");
    const applyFilters = document.getElementById("applyFilters");
    const resetFilters = document.getElementById("resetFilters");
    const sortPosts = document.getElementById("sortPosts");
    const postCount = document.getElementById("postCount");
    const emptyMessage = document.getElementById("emptyMessage");

    if (!postList) {
        console.error("postList 요소를 찾을 수 없습니다.");
        return;
    }

    const cards = Array.from(postList.querySelectorAll(".postCard"));
    
    function filterPosts() {

        const keyword = keywordSearch
            ? keywordSearch.value.trim().toLowerCase()
            : "";

        const selectedSmoking = Array.from(
            document.querySelectorAll('input[name="smoking"]:checked')
        ).map(function (input) {
            return input.value;
        });

        const selectedHabits = Array.from(
            document.querySelectorAll('input[name="habit"]:checked')
        ).map(function (input) {
            return input.value;
        });

        let visibleCount = 0;

        cards.forEach(function (card) {

            const text = card.textContent.toLowerCase();
            const smoking = card.dataset.smoking;
            const habits = card.dataset.habits || "";

            const keywordMatch = !keyword || text.includes(keyword);

            const smokingMatch =
                selectedSmoking.length === 0 ||
                selectedSmoking.includes(smoking);

            const habitsMatch = selectedHabits.every(function (habit) {
                return habits.replace(/\s+/g, "").includes(
                    habit.replace(/\s+/g, "")
                );
            });

            const visible = keywordMatch && smokingMatch && habitsMatch;

            card.hidden = !visible;

            if (visible) {
                visibleCount++;
            }
        });

        if (postCount) {
            postCount.textContent = visibleCount;
        }

        if (emptyMessage) {
            emptyMessage.hidden = visibleCount !== 0;
        }
    }
    
    if (applyFilters) {
        applyFilters.addEventListener("click", filterPosts);
    }
    
    if (sortPosts) {
        sortPosts.addEventListener("change", function () {

            const sortedCards = cards.slice();

            sortedCards.sort(function (a, b) {

                const aDate = Number(a.dataset.date);
                const bDate = Number(b.dataset.date);

                return sortPosts.value === "latest"
                    ? bDate - aDate
                    : aDate - bDate;
            });

            sortedCards.forEach(function (card) {
                postList.appendChild(card);
            });
        });
    }
    
    postList.addEventListener("click", function (event) {

        const card = event.target.closest(".postCard");

        if (!card) {
            return;
        }

        if (typeof window.openDetailModal === "function") {
            window.openDetailModal(card);
        } else {
            console.error(
                "openDetailModal 함수를 찾을 수 없습니다. detail.js를 확인하세요."
            );
        }
    });
	
    filterPosts();

});
