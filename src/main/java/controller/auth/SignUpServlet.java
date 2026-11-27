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

// 회원가입 요청 처리 Controller
@WebServlet("/app/signup")
public class SignUpServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/auth/signup.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");

        try {
            User user = userService.signup(
                    email, password, passwordConfirm);

            request.getSession().setAttribute("loginMember", user);
            request.changeSessionId();

            response.sendRedirect(
                    request.getContextPath() + "/app/onboarding");

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());
            doGet(request, response);

        } catch (SQLException e) {

            log("회원가입 처리 실패", e);
            request.setAttribute("error",
                    "회원가입 처리 중 오류가 발생했습니다.");
            doGet(request, response);
        }
    }
}