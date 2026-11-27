package controller.mypage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import controller.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.mypage.ProfileView;
import model.service.MyPageService;
import model.service.UserNotFoundException;
import util.JsonUtil;
import util.SessionUtil;

/** 마이페이지 Command 공통 기능 */
public abstract class MyPageCommand implements Command {

    protected static final String FLASH = "mypageFlash";
    protected static final String LOGIN_PAGE = "/views/auth/login.jsp";

    protected final MyPageService service = MyPageService.getInstance();

    /** FrontController 에서 로그인 검사를 하므로 여기서는 항상 값이 있다 */
    protected int loginId(HttpServletRequest request) {
        return SessionUtil.getLoginStudentId(request);
    }

    /**
     * 사이드바 등 모든 마이페이지 화면에서 쓰는 로그인 사용자 정보를 request 의 "me" 로 저장.
     * 세션은 있는데 DB 에 사용자가 없으면(탈퇴 등) null 반환 → 호출한 쪽에서 로그인 화면으로 보낸다.
     */
    protected ProfileView loadMe(HttpServletRequest request) throws SQLException {
        try {
            ProfileView me = service.getMe(loginId(request));
            request.setAttribute("me", me);
            return me;
        } catch (UserNotFoundException e) {
            SessionUtil.logout(request);
            return null;
        }
    }

    /** 화면 이동 후 한 번만 보여줄 메시지 (PRG 패턴) */
    protected void setFlash(HttpServletRequest request, String type, String message) {
        Map<String, String> flash = new HashMap<String, String>();
        flash.put("type", type);       // "success" | "error"
        flash.put("message", message);
        request.getSession().setAttribute(FLASH, flash);
    }

    protected void moveFlashToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute(FLASH);
            if (flash != null) {
                request.setAttribute("flash", flash);
                session.removeAttribute(FLASH);
            }
        }
    }

    protected int intParam(HttpServletRequest request, String name, int defaultValue) {
        String v = request.getParameter(name);
        if (v == null) return defaultValue;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    protected String json(HttpServletResponse response, boolean ok, String message) throws IOException {
        return json(response, ok, message, null);
    }

    protected String json(HttpServletResponse response, boolean ok, String message, Map<String, Object> extra)
            throws IOException {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("ok", ok);
        if (message != null) body.put("message", message);
        if (extra != null) body.putAll(extra);
        JsonUtil.send(response, ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST, body);
        return null;
    }
}
