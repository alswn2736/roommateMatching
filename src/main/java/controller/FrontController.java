package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JsonUtil;
import util.SessionUtil;
import util.GroupCsrf;

/**
 * 모든 요청을 받아 RequestMapping 에서 Command 를 찾아 실행하고,
 * Command 가 돌려준 값에 따라 forward / redirect 한다.
 *
 * Command 반환값 규칙
 *   "/views/xxx.jsp"     → forward
 *   "redirect:/mypage/x" → 컨텍스트 경로를 붙여 redirect
 *   null                 → Command 가 응답(JSON 등)을 직접 작성함
 */
@WebServlet(name = "FrontController", urlPatterns = { "/mypage", "/mypage/*" }, loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024 * 1024,       // 1MB 넘으면 임시파일로
                 maxFileSize = 5 * 1024 * 1024,          // 파일 1개 5MB
                 maxRequestSize = 6 * 1024 * 1024)       // 요청 전체 6MB
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LOGIN_PAGE = "/app/login"; // auth LoginServlet

    private RequestMapping requestMapping;

    @Override
    public void init() throws ServletException {
        requestMapping = new RequestMapping();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI().substring(contextPath.length());
        if (uri.length() > 1 && uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        // /mypage 로 들어오면 프로필 화면으로
        if ("/mypage".equals(uri)) {
            response.sendRedirect(contextPath + "/mypage/profile");
            return;
        }

        // 마이페이지는 전부 로그인 필요
        if (uri.startsWith("/mypage") && !SessionUtil.isLogin(request)) {
            if (isAjax(request)) {
                Map<String, Object> body = new HashMap<String, Object>();
                body.put("ok", false);
                body.put("message", "로그인이 필요합니다.");
                body.put("redirect", contextPath + LOGIN_PAGE);
                JsonUtil.send(response, HttpServletResponse.SC_UNAUTHORIZED, body);
            } else {
                response.sendRedirect(contextPath + LOGIN_PAGE);
            }
            return;
        }

        Command command = requestMapping.find(request.getMethod(), uri);
        if (command == null) {
            if (requestMapping.hasUri(uri)) {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        // 그룹 생성/퇴실은 그룹 화면과 같은 CSRF 검사로 보호한다.
        boolean groupMutation = "POST".equals(request.getMethod())
                && ("/mypage/matched/leave".equals(uri)
                    || "/mypage/applicants/accept".equals(uri)
                    || "/mypage/applicants/reject".equals(uri));
        if (groupMutation && !GroupCsrf.isValid(request)) {
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("ok", false);
            body.put("message", "요청 확인에 실패했습니다. 화면을 새로고침해 주세요.");
            JsonUtil.send(response, HttpServletResponse.SC_FORBIDDEN, body);
            return;
        }
        if ("GET".equals(request.getMethod())
                && ("/mypage/matched".equals(uri) || "/mypage/applicants".equals(uri))) {
            response.setHeader("Cache-Control", "no-store");
            request.setAttribute("groupCsrf", GroupCsrf.token(request.getSession()));
        }

        String result;
        try {
            result = command.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            if (isAjax(request) && !response.isCommitted()) {
                Map<String, Object> body = new HashMap<String, Object>();
                body.put("ok", false);
                body.put("message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
                JsonUtil.send(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, body);
                return;
            }
            throw new ServletException(e);
        }

        if (result == null || response.isCommitted()) {
            return;
        }
        if (result.startsWith("redirect:")) {
            response.sendRedirect(contextPath + result.substring("redirect:".length()));
        } else {
            request.getRequestDispatcher(result).forward(request, response);
        }
    }

    public static boolean isAjax(HttpServletRequest request) {
        String xrw = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        return "XMLHttpRequest".equals(xrw) || (accept != null && accept.contains("application/json"));
    }
}
