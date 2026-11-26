package controller.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 로그인 요청 처리 controller
@WebServlet("/app/login")
public class LoginServlet extends HttpServlet {
	 @Override
	    protected void doGet(HttpServletRequest request,
	            HttpServletResponse response)
	            throws ServletException, IOException {

	        request.getRequestDispatcher("/views/auth/login.jsp")
	                .forward(request, response);
	    }
}
