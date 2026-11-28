package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** 그룹 화면과 마이페이지의 그룹 변경 요청이 공유하는 세션 토큰. */
public final class GroupCsrf {
    private static final String ATTRIBUTE = "group.csrf";

    private GroupCsrf() { }

    public static String token(HttpSession session) {
        synchronized (session) {
            String token = (String) session.getAttribute(ATTRIBUTE);
            if (token == null) {
                byte[] bytes = new byte[32];
                new SecureRandom().nextBytes(bytes);
                token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
                session.setAttribute(ATTRIBUTE, token);
            }
            return token;
        }
    }

    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String expected = session == null ? null : (String) session.getAttribute(ATTRIBUTE);
        String actual = request.getHeader("X-CSRF-Token");
        if (actual == null) actual = request.getParameter("csrf");
        return expected != null && actual != null
                && MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
                        actual.getBytes(StandardCharsets.UTF_8));
    }
}
