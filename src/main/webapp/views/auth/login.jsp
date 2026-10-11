<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&family=Noto+Sans+KR:wght@400;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/auth/login.css">

</head>
<body>
	<main class="login-page">
		<div class="login-card">
			<section class="login-intro">
				<div class="intro-logo d-flex align-items-center gap-2">
					<img
						src="${pageContext.request.contextPath}/assets/image/auth/user.svg"
						width="24" height="24" alt=""> <span>룸메이트</span>
				</div>

				<div class="intro-description">
					<h1>
						우리 학교 룸메이트를 찾는<br>가장 쉬운 방법
					</h1>
					<p>생활 습관, 수면 패턴, 학과 매칭으로 나와 꼭 맞는 단짝을 찾고 더 나은 기숙사 생활을 시작해 보세요.</p>
				</div>

				<img class="intro-image"
					src="${pageContext.request.contextPath}/assets/image/auth/room.png"
					alt="기숙사 방 이미지">
			</section>

			<section class="login-form-section">
				<div class="form-heading">
					<h2>다시 오셨네요</h2>
					<p>룸메이트 계정으로 로그인해주세요.</p>
				</div>

				<form id="loginForm" class="login-form">
					<div class="login-fields">
						<div class="login-field">
							<label for="schoolEmail" class="form-label">학교 이메일</label> <input
								type="email" class="form-control login-input" id="schoolEmail"
								name="email" placeholder="student@university.ac.kr" required>
						</div>

						<div class="login-field">
							<label for="password" class="form-label">비밀번호</label>
							<div class="password-wrapper">
								<input type="password" class="form-control login-input"
									id="password" name="password" required>

								<button type="button" class="password-toggle"
									id="passwordToggle" aria-label="비밀번호 표시">
									<i id="eyeIcon" class="bi bi-eye-slash"></i>
								</button>
							</div>
						</div>
					</div>

					<div class="login-actions">
						<button type="submit" class="btn login-submit">로그인</button>
						<p class="signup-guide">
							처음이신가요? <a
								href="${pageContext.request.contextPath}/views/auth/signup.jsp">회원가입하기</a>
						</p>
					</div>
				</form>
			</section>

		</div>
	</main>

	<script>
		document.getElementById("passwordToggle").addEventListener(
				"click",
				function() {
					const password = document.getElementById("password");
					const eyeIcon = document.getElementById("eyeIcon");

					const isHidden = password.type === "password";
					password.type = isHidden ? "text" : "password";
					eyeIcon.className = isHidden ? "bi bi-eye"
							: "bi bi-eye-slash";
				});

	</script>
</body>
</html>
