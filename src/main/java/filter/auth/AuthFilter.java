package filter.auth;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인증 JSP 직접 접근 방지 Filter
@WebFilter(urlPatterns = {
    "/views/auth/login.jsp",
    "/views/auth/signup.jsp",
    "/views/auth/onboarding.jsp"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        if (path.equals("/views/auth/login.jsp")) {
            res.sendRedirect(req.getContextPath() + "/app/login");

        } else if (path.equals("/views/auth/signup.jsp")) {
            res.sendRedirect(req.getContextPath() + "/app/signup");

        } else if (path.equals("/views/auth/onboarding.jsp")) {
            res.sendRedirect(req.getContextPath() + "/app/onboarding");
        }
    }
}