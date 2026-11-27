package controller.auth;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.domain.User;
import model.service.UserService;

// 로그인 요청 처리 controller
@WebServlet("/app/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/auth/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userService.login(email, password);

            request.getSession().setAttribute("loginMember", user);
            request.changeSessionId();

            if (user.isProfileCompleted()) {
                response.sendRedirect(
                        request.getContextPath() + "/app/feed");
            } else {
                response.sendRedirect(
                        request.getContextPath() + "/app/onboarding");
            }

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());
            doGet(request, response);

        } catch (SQLException e) {

            log("로그인 처리 실패", e);
            request.setAttribute("error",
                    "로그인 처리 중 오류가 발생했습니다.");
            doGet(request, response);
        }
    }
}