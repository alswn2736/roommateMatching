
document.addEventListener("DOMContentLoaded", function () {

    const overlay = document.getElementById("detailModalOverlay");
    const closeButton = document.getElementById("closeDetailModal");
    const applyButton = document.getElementById("openApplyModal");

    if (!overlay) {
        console.error("detailModalOverlay를 찾을 수 없습니다.");
        return;
    }

    const detailTitle = document.getElementById("detailTitle");
    const detailRoomType = document.getElementById("detailRoomType");
    const detailAuthorName = document.getElementById("detailAuthorName");
    const detailAuthorInfo = document.getElementById("detailAuthorInfo");
    const detailSmoking = document.getElementById("detailSmoking");
    const detailSleep = document.getElementById("detailSleep");
    const detailDescription = document.getElementById("detailDescription");
    const tagContainer = document.getElementById("detailTags");

    const avatarImage = document.getElementById("detailAvatarImage");
    const avatarFallback = document.getElementById("detailAvatarFallback");
	
    const defaultDescription =
        "새 학기를 함께할 조용하고 깔끔한 룸메이트를 찾고 있습니다. 아침형 생활 패턴을 가지신 분과 좋은 메이트가 되고 싶어요! 시험 기간에도 서로 조용하며 배려할 줄 아는 분이면 좋겠습니다. 편하게 연락주세요!";
		
    window.openDetailModal = function (card) {

        if (!card) return;
		
        if (detailTitle) {
            detailTitle.textContent = card.dataset.title || "";
        }
		
        if (detailRoomType) {
            detailRoomType.textContent = card.dataset.room || "";
        }
		
        if (detailAuthorName) {
            detailAuthorName.textContent = card.dataset.author || "";
        }
		
        if (detailAuthorInfo) {
            detailAuthorInfo.textContent = card.dataset.authorInfo || "";
        }
		
        if (avatarImage && avatarFallback) {

            avatarFallback.textContent = card.dataset.avatar || "룸";

            avatarImage.hidden = false;
            avatarFallback.hidden = true;

            avatarImage.onerror = function () {
                avatarImage.hidden = true;
                avatarFallback.hidden = false;
            };
        }
		
        if (detailSmoking) {
            detailSmoking.textContent =
                card.dataset.smoking || "미입력";
        }
		
        const habits = (card.dataset.habits || "")
            .split(/\s+/)
            .filter(Boolean);

        if (detailSleep) {
            detailSleep.textContent =
                habits.includes("아침형") ? "아침형 (6~7시 기상)" :
                habits.includes("저녁형") ? "저녁형" :
                "미입력";
        }
		
        const description = card.querySelector(".postDescription");

        if (detailDescription) {
            detailDescription.textContent =
                description && description.textContent.trim()
                    ? description.textContent.trim()
                    : defaultDescription;
        }
		
        if (tagContainer) {
            tagContainer.replaceChildren();

            const tags = card.querySelectorAll(".postTags span");

            if (tags.length > 0) {
                tags.forEach(function (tag) {
                    const span = document.createElement("span");
                    span.textContent = tag.textContent;
                    tagContainer.appendChild(span);
                });
            } else {
                ["#아침형", "#비흡연", "#깔끔", "#조용한 편"]
                    .forEach(function (tagText) {
                        const span = document.createElement("span");
                        span.textContent = tagText;
                        tagContainer.appendChild(span);
                    });
            }
        }
		
        overlay.dataset.postId = card.dataset.id || "";
		
        overlay.hidden = false;
        document.body.style.overflow = "hidden";
    };
	
    function closeDetail() {
        overlay.hidden = true;
        document.body.style.overflow = "";
    }

    if (closeButton) {
        closeButton.addEventListener("click", closeDetail);
    }
	
    overlay.addEventListener("click", function (event) {
        if (event.target === overlay) {
            closeDetail();
        }
    });
	
    if (applyButton) {
        applyButton.addEventListener("click", function () {

            const applyOverlay =
                document.getElementById("applyModalOverlay");

            if (!applyOverlay) {
                console.error("applyModalOverlay를 찾을 수 없습니다.");
                return;
            }

            applyOverlay.dataset.postId =
                overlay.dataset.postId || "";

            applyOverlay.hidden = false;
            overlay.hidden = true;

            document.body.style.overflow = "hidden";
        });
    }
});
