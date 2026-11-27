document.addEventListener("DOMContentLoaded", function () {

    const overlay = document.getElementById("applyModalOverlay");
    const message = document.getElementById("applyMessage");
    const count = document.getElementById("applyCharacterCount");

    const cancelButton = document.getElementById("cancelApplyModal");
	
	const form = document.getElementById("applyForm");
	const postIdInput = document.getElementById("applyPostId");
	
    if (!overlay || !message) return;
    
    function updateCount() {
        if (count) {
            count.textContent = message.value.length;
        }
    }
    
    function closeApply() {
        overlay.hidden = true;
        document.body.style.overflow = "";
       
        const detailOverlay =
            document.getElementById("detailModalOverlay");
			
        if (detailOverlay) {
            detailOverlay.hidden = false;
            document.body.style.overflow = "hidden";
        }
    }
	
    message.addEventListener("input", updateCount);
   
    if (cancelButton) {
        cancelButton.addEventListener("click", closeApply);
    }
	
    overlay.addEventListener("click", function (event) {
        if (event.target === overlay) {
            closeApply();
        }
    });

	if (form) {
	    form.addEventListener("submit", function (event) {
	        const postId = overlay.dataset.postId;

	        if (!postId) {
	            event.preventDefault();
	            alert("신청할 모집글을 찾을 수 없습니다.");
	            return;
	        }

	        postIdInput.value = postId;

	        if (!message.value.trim()) {
	            event.preventDefault();
	            alert("보낼 한 마디를 입력해 주세요.");
	            message.focus();
	            return;
	        }

	        if (message.value.length > 300) {
	            event.preventDefault();
	            alert("메시지는 300자 이내로 작성해 주세요.");
	            message.focus();
	            return;
	        }
	    });
	}
	
    updateCount();
});