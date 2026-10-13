const profileImage = document.getElementById("profileImage");
const profilePhoto = document.getElementById("profilePhoto");
const defaultPhoto = profilePhoto.src;

profileImage.addEventListener("change", function() {
    const file = profileImage.files[0];

    if (!file) return;

    if (!["image/jpeg", "image/png"].includes(file.type)) {
        alert("JPG 또는 PNG 이미지를 선택해주세요.");
        profileImage.value = "";
        profilePhoto.src = defaultPhoto;
        return;
    }

    profilePhoto.src = URL.createObjectURL(file);
});

document.getElementById("imageReset").addEventListener("click", function() {
    profileImage.value = "";
    profilePhoto.src = defaultPhoto;
});

document.querySelectorAll(".privacy-switch").forEach(function(toggle) {
    const privacyText = document.getElementById(toggle.dataset.label);

    function updateText() {
        privacyText.textContent = toggle.checked ? "공개" : "비공개";
    }

    toggle.addEventListener("change", updateText);
    updateText();
});