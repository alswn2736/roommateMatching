document.addEventListener("DOMContentLoaded", function () {

    const postList = document.getElementById("postList");

    if (!postList) {
        console.error("postList 요소를 찾을 수 없습니다.");
        return;
    }

    const cards = Array.from(postList.querySelectorAll(".postCard"));

    // 등록일을 현재 시간 기준으로 표시
    function formatRelativeDate(date) {

        const now = new Date();
        const diff = now - date;

        const minutes = Math.floor(diff / (1000 * 60));
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const days = Math.floor(diff / (1000 * 60 * 60 * 24));

        if (minutes < 1) {
            return "방금 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 8) {
            return days + "일 전";
        } else {
            return date.getFullYear() + "." +
                String(date.getMonth() + 1).padStart(2, "0") + "." +
                String(date.getDate()).padStart(2, "0");
        }
    }

    // 모든 게시글의 등록일 표시
    cards.forEach(function (card) {

        const date = new Date(Number(card.dataset.date));
        const relativeDate = card.querySelector(".relativeDate");

        if (relativeDate) {
            relativeDate.textContent = formatRelativeDate(date);
        }
    });

    // 게시글 클릭 시 상세 모달 열기
    postList.addEventListener("click", function (event) {
		// 수정 버튼 클릭 시 상세 모달을 열지 않음
		if (event.target.closest(".editPostButton")) {
		    return;
		}
		
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

});