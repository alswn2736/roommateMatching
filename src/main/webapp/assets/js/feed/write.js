document.addEventListener("DOMContentLoaded", function () {
    const overlay = document.getElementById("writeModalOverlay");
    const openButton = document.getElementById("openWriteModal");
    const closeButton = document.getElementById("closeWriteModal");
    const cancelButton = document.getElementById("cancelWriteModal");
    const form = document.getElementById("writeForm");
	
    if (!overlay || !openButton || !form) {
        console.error("글쓰기 모달 요소를 찾을 수 없습니다.");
        return;
    }
	
    function openModal() {
        overlay.hidden = false;
        document.body.style.overflow = "hidden";

        const titleInput = document.getElementById("recruitTitle");
        if (titleInput) {
            titleInput.focus();
        }
    }
	
    function closeModal() {
        overlay.hidden = true;
        document.body.style.overflow = "";
    }

    openButton.addEventListener("click", openModal);

    if (closeButton) {
        closeButton.addEventListener("click", closeModal);
    }

    if (cancelButton) {
        cancelButton.addEventListener("click", closeModal);
    }
	
	overlay.addEventListener("click", function (event) {
	    if (event.target === overlay) {
	        closeModal();
	    }
	});
	
    overlay.querySelectorAll(".roomTypeOption input").forEach(function (input) {
        input.addEventListener("change", function () {
            overlay.querySelectorAll(".roomTypeOption").forEach(function (label) {
                const radio = label.querySelector("input");
                label.classList.toggle("selected", radio.checked);
            });
        });
    });
	
    overlay.querySelectorAll(".habitTag input").forEach(function (input) {
        input.addEventListener("change", function () {
            input.closest(".habitTag").classList.toggle(
                "selected",
                input.checked
            );
        });
    });
	
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        if (!form.reportValidity()) {
            return;
        }
    });
	
});