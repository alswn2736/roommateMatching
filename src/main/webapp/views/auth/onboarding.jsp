<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>프로필 설정</title>
<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
    rel="stylesheet">
<link
    href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&family=Newsreader:wght@700&display=swap"
    rel="stylesheet">
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/auth/onboarding.css">
</head>
<body>
    <main class="onboarding-page">
        <section class="onboarding-card">
            <div class="onboarding-heading">
                <h1>프로필 설정</h1>
                <p>매칭에 사용할 기본 정보를 입력해주세요.</p>
            </div>

            <form id="onboardingForm" class="onboarding-form">
                <div class="profile-photo-section">
                    <img id="profilePhoto" class="profile-photo"
                        src="${pageContext.request.contextPath}/assets/image/auth/profile-default.png"
                        alt="프로필 사진">

                    <div class="profile-photo-actions">
                        <div class="d-flex align-items-center gap-2">
                            <label for="profileImage" class="btn image-select">
                                이미지 선택
                            </label>

                            <input type="file" id="profileImage" name="profileImage"
                                class="d-none" accept="image/jpeg,image/png">

                            <button type="button" id="imageReset" class="image-reset">
                                초기화
                            </button>
                        </div>

                        <p class="image-guide">권장 규격: 200×200px (JPG, PNG)</p>
                    </div>
                </div>

                <div class="onboarding-fields">
                    <div class="onboarding-field">
                        <label for="nickname" class="form-label">닉네임</label>
                        <input type="text" id="nickname" name="nickname"
                            class="form-control auth-input"
                            placeholder="멋진 단짝" required>
                    </div>

                    <div class="onboarding-field">
                        <div class="field-heading">
                            <label for="name" class="form-label">이름</label>

                            <div class="privacy-control">
                                <span id="namePrivacyText">공개</span>
                                <div class="form-check form-switch">
                                    <input type="checkbox" id="namePublic"
                                        name="namePublic" value="true"
                                        class="form-check-input privacy-switch"
                                        role="switch" aria-label="이름 공개"
                                        data-label="namePrivacyText" checked>
                                </div>
                            </div>
                        </div>

                        <input type="text" id="name" name="name"
                            class="form-control auth-input"
                            placeholder="김민재" required>
                    </div>

                    <div class="onboarding-field">
                        <div class="field-heading">
                            <label for="studentNumber" class="form-label">학번</label>

                            <div class="privacy-control">
                                <span id="studentPrivacyText">공개</span>
                                <div class="form-check form-switch">
                                    <input type="checkbox" id="studentPublic"
                                        name="studentPublic" value="true"
                                        class="form-check-input privacy-switch"
                                        role="switch" aria-label="학번 공개"
                                        data-label="studentPrivacyText" checked>
                                </div>
                            </div>
                        </div>

                        <input type="text" id="studentNumber" name="studentNumber"
                            class="form-control auth-input"
                            placeholder="20학번 (3학년)" required>
                    </div>

                    <div class="onboarding-field">
                        <div class="field-heading">
                            <label for="department" class="form-label">학과</label>

                            <div class="privacy-control">
                                <span id="departmentPrivacyText">비공개</span>
                                <div class="form-check form-switch">
                                    <input type="checkbox" id="departmentPublic"
                                        name="departmentPublic" value="true"
                                        class="form-check-input privacy-switch"
                                        role="switch" aria-label="학과 공개"
                                        data-label="departmentPrivacyText">
                                </div>
                            </div>
                        </div>

                        <input type="text" id="department" name="department"
                            class="form-control auth-input"
                            placeholder="컴퓨터공학과" required>
                    </div>
                </div>

                <button type="submit" class="btn auth-submit">
                    완료하고 메이트 매칭 시작하기
                </button>
            </form>
        </section>
    </main>

    <script
        src="${pageContext.request.contextPath}/assets/js/auth/onboarding.js"></script>
</body>
</html>