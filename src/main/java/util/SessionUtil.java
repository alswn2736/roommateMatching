package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.domain.User;
import model.dto.UserDTO;

/**
 * 로그인 세션 관련 공통 처리.
 *
 * auth 의 LoginServlet 이 로그인 성공 시
 *   session.setAttribute("loginMember", user);   // model.domain.User
 * 로 저장한다. 마이페이지는 여기서 학번만 꺼내서 DB 에서 최신 정보를 다시 조회한다.
 */
public class SessionUtil {

    /** auth(LoginServlet)와 같은 세션 키 */
    public static final String LOGIN_USER = "loginMember";

    private SessionUtil() {}

    /** 로그인한 사용자의 학번. 로그인 안 되어 있으면 null */
    public static Integer getLoginStudentId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(LOGIN_USER);

        int id = 0;
        if (value instanceof User) {
            id = ((User) value).getStudentId();
        } else if (value instanceof UserDTO) {
            id = ((UserDTO) value).getStudentId();
        } else if (value instanceof Number) {
            id = ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                id = Integer.parseInt(((String) value).trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return id > 0 ? id : null;
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
