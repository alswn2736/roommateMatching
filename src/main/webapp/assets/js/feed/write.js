document.addEventListener("DOMContentLoaded", function () {
    const overlay = document.getElementById("writeModalOverlay");
    const openButton = document.getElementById("openWriteModal");
    const closeButton = document.getElementById("closeWriteModal");
    const cancelButton = document.getElementById("cancelWriteModal");
    const form = document.getElementById("writeForm");
    const postList = document.getElementById("postList");

    if (!overlay || !openButton || !form) {
        console.error("글쓰기 모달 요소를 찾을 수 없습니다.");
        return;
    }

    const titleInput = document.getElementById("recruitTitle");
    const postIdInput = document.getElementById("writePostId");
    const descriptionInput = form.querySelector('[name="description"]');
    const modalTitle = document.getElementById("writeModalTitle");
    const submitButton = document.getElementById("writeSubmitButton");
	const deleteButton = document.getElementById("deletePostButton");

    const contextPath = form.action.split("/app/")[0];

    // 선택 스타일 갱신
    function updateSelectedStyle() {
        overlay.querySelectorAll(".roomTypeOption").forEach(function (label) {
            const radio = label.querySelector("input");
            label.classList.toggle("selected", radio.checked);
        });

        overlay.querySelectorAll(".habitTag").forEach(function (label) {
            const checkbox = label.querySelector("input");
            label.classList.toggle("selected", checkbox.checked);
        });
    }

    // 글쓰기 모달 초기화
    function resetForm() {
        form.reset();

        if (postIdInput) postIdInput.value = "";

        const doubleRoom = form.querySelector(
            'input[name="roomType"][value="DOUBLE"]'
        );

        if (doubleRoom) doubleRoom.checked = true;

        form.action = contextPath + "/app/feed/write";

        if (modalTitle) modalTitle.textContent = "새 메이트 모집글 작성";
        if (submitButton) submitButton.textContent = "모집글 등록하기";
		if (deleteButton) deleteButton.hidden = true;

        updateSelectedStyle();
    }

    // 글쓰기 모달 열기
    function openModal() {
        resetForm();

        overlay.hidden = false;
        document.body.style.overflow = "hidden";

        if (titleInput) titleInput.focus();
    }

    // 수정 모달 열기
    function openEditModal(card) {
        if (!card) return;

        resetForm();

        if (postIdInput) postIdInput.value = card.dataset.id || "";
        if (titleInput) titleInput.value = card.dataset.title || "";

        const roomType = form.querySelector(
            'input[name="roomType"][value="' + card.dataset.roomType + '"]'
        );

        if (roomType) roomType.checked = true;

        const habits = {
            "아침형": card.dataset.kwSleeptime === "M",
            "저녁형": card.dataset.kwSleeptime === "E",
            "비흡연자": card.dataset.kwSmoking === "N",
            "흡연 가능": card.dataset.kwSmoking === "Y",
            "조용한 편": card.dataset.kwSensitivity === "Q",
            "사교적": card.dataset.kwSensitivity === "S",
            "에어컨 빵빵": card.dataset.kwAircon === "Y",
            "위생청결 철저": card.dataset.kwClean === "Y"
        };

        form.querySelectorAll('input[name="habits"]').forEach(function (input) {
            input.checked = habits[input.value] || false;
        });

        if (descriptionInput) {
            descriptionInput.value = card.dataset.content || "";
        }

        form.action = contextPath + "/app/feed/update";

        if (modalTitle) modalTitle.textContent = "모집글 수정";
        if (submitButton) submitButton.textContent = "수정하기";
		if (deleteButton) deleteButton.hidden = false;

        updateSelectedStyle();

        overlay.hidden = false;
        document.body.style.overflow = "hidden";

        if (titleInput) titleInput.focus();
    }

    // 모달 닫기
    function closeModal() {
        overlay.hidden = true;
        document.body.style.overflow = "";
    }

    // 글쓰기 버튼
    openButton.addEventListener("click", openModal);

	// 수정 버튼
	if (postList) {
	    postList.addEventListener("click", function (event) {
	        const editButton = event.target.closest(".editPostButton");

	        if (!editButton) return;

	        event.preventDefault();
	        event.stopPropagation();

	        const card = editButton.closest(".postCard");
	        openEditModal(card);
	    });
	}

    if (closeButton) {
        closeButton.addEventListener("click", closeModal);
    }

    if (cancelButton) {
        cancelButton.addEventListener("click", closeModal);
    }
	
	if (deleteButton) {
	    deleteButton.addEventListener("click", function () {
	        if (!confirm("정말 모집글을 삭제하시겠습니까?")) {
	            return;
	        }

	        const postId = postIdInput.value;

	        const deleteForm = document.createElement("form");
	        deleteForm.method = "POST";
	        deleteForm.action = contextPath + "/app/feed/delete";

	        const input = document.createElement("input");
	        input.type = "hidden";
	        input.name = "postId";
	        input.value = postId;

	        deleteForm.appendChild(input);
	        document.body.appendChild(deleteForm);
	        deleteForm.submit();
	    });
	}

    overlay.addEventListener("click", function (event) {
        if (event.target === overlay) {
            closeModal();
        }
    });

    // 방 종류 선택 스타일
    overlay.querySelectorAll(".roomTypeOption input").forEach(function (input) {
        input.addEventListener("change", updateSelectedStyle);
    });

    // 생활 습관 선택 스타일
    overlay.querySelectorAll(".habitTag input").forEach(function (input) {
        input.addEventListener("change", updateSelectedStyle);
    });
});