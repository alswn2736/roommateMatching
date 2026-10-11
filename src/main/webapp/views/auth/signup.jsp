<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&family=Noto+Sans+KR:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth/signup.css">
</head>
<body>
    <main class="signup-page">
        <div class="signup-card">
            <section class="signup-intro">
                <div class="intro-logo d-flex align-items-center gap-2">
                    <img src="${pageContext.request.contextPath}/assets/image/auth/user.svg"
                        width="24" height="24" alt="">
                    <span>룸메이트</span>
                </div>

                <div class="intro-description">
                    <h1>나와 똑똑한 메이트의 시작</h1>
                    <p>간단한 가입만으로 검증된 학교 친구들과 라이프스타일을 확인해 볼 수 있습니다.</p>
                </div>

                <img class="intro-image"
                    src="${pageContext.request.contextPath}/assets/image/auth/signup-room.png"
                    alt="기숙사 방 이미지">
            </section>

            <section class="signup-form-section">
                <div class="form-heading">
                    <h2>시작하기</h2>
                    <p>학교 이메일로 가입을 환영합니다.</p>
                </div>

                <form id="signupForm" class="signup-form">
                    <div class="signup-fields">
                        <div class="signup-field">
                            <label for="schoolEmail" class="form-label">학교 이메일</label>
                            <input type="email" class="form-control signup-input"
                                id="schoolEmail" name="email"
                                placeholder="student@university.ac.kr" required>
                        </div>

                        <div class="signup-field">
                            <label for="password" class="form-label">비밀번호</label>
                            <div class="password-wrapper">
                                <input type="password" class="form-control signup-input"
                                    id="password" name="password" placeholder="8자리 이상 입력"
                                    minlength="8" autocomplete="new-password" required>
                                <button type="button" class="password-toggle"
                                    aria-label="비밀번호 표시" aria-pressed="false">
                                    <i class="bi bi-eye-slash" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>

                        <div class="signup-field">
                            <label for="passwordConfirm" class="form-label">비밀번호 확인</label>
                            <div class="password-wrapper">
                                <input type="password" class="form-control signup-input"
                                    id="passwordConfirm" name="passwordConfirm"
                                    placeholder="비밀번호 재입력" minlength="8"
                                    autocomplete="new-password" required>
                                <button type="button" class="password-toggle"
                                    aria-label="비밀번호 확인 표시" aria-pressed="false">
                                    <i class="bi bi-eye-slash" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="signup-actions">
                        <button type="submit" class="btn signup-submit">가입하기</button>
                        <p class="login-guide">
                            이미 계정이 있으신가요?
                            <a href="${pageContext.request.contextPath}/views/auth/login.jsp">로그인하기</a>
                        </p>
                    </div>
                </form>
            </section>
        </div>
    </main>

    <script>
    document.querySelectorAll(".password-toggle").forEach(function(button) {
        const label = button.getAttribute("aria-label");
        button.addEventListener("click", function() {
            const password = button.previousElementSibling;
            const eyeIcon = button.querySelector("i");
            const isHidden = password.type === "password";

            password.type = isHidden ? "text" : "password";
            eyeIcon.className = isHidden ? "bi bi-eye" : "bi bi-eye-slash";
            button.setAttribute("aria-label", isHidden ? label.replace("표시", "숨기기") : label);
            button.setAttribute("aria-pressed", String(isHidden));
        });
    });

    const password = document.getElementById("password");
    const passwordConfirm = document.getElementById("passwordConfirm");

    function checkPassword() {
        passwordConfirm.setCustomValidity(
            password.value === passwordConfirm.value ? "" : "비밀번호가 일치하지 않습니다."
        );
    }

    password.addEventListener("input", checkPassword);
    passwordConfirm.addEventListener("input", checkPassword);
    </script>
</body>
</html>
