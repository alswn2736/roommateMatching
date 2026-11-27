package controller.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//최초 프로필 설정 요청 처리 Controller
@WebServlet("/app/onboarding")
public class OnboardingServlet extends HttpServlet {

 @Override
 protected void doGet(HttpServletRequest request,
         HttpServletResponse response)
         throws ServletException, IOException {

     HttpSession session = request.getSession(false);

     if (session == null ||
             session.getAttribute("loginMember") == null) {

         response.sendRedirect(
                 request.getContextPath() + "/app/login");
         return;
     }

     request.getRequestDispatcher("/views/auth/onboarding.jsp")
             .forward(request, response);
 }
}
