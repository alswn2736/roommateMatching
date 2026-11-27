package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dto.UserDTO;

/**
 * 로그인 세션 관련 공통 처리.
 *
 * 로그인 성공 시 (auth 쪽 로그인 처리에서) 아래 둘 중 하나로 세션에 저장하면 된다.
 *   session.setAttribute(SessionUtil.LOGIN_USER, userDTO);        // UserDTO 통째로
 *   session.setAttribute(SessionUtil.LOGIN_USER, user.getStudentId()); // 학번만
 * 마이페이지는 어느 방식이든 학번만 꺼내서 DB 에서 최신 정보를 다시 조회한다.
 */
public class SessionUtil {

    public static final String LOGIN_USER = "loginUser";

    private SessionUtil() {}

    /** 로그인한 사용자의 학번. 로그인 안 되어 있으면 null */
    public static Integer getLoginStudentId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(LOGIN_USER);

        if (value instanceof UserDTO) {
            int id = ((UserDTO) value).getStudentId();
            return id == 0 ? null : id;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt(((String) value).trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean isLogin(HttpServletRequest request) {
        return getLoginStudentId(request) != null;
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
