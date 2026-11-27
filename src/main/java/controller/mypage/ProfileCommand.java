package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** GET /mypage/profile : 프로필 관리 화면 */
public class ProfileCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (loadMe(request) == null) {
            return "redirect:" + LOGIN_PAGE;
        }
        moveFlashToRequest(request);
        request.setAttribute("menu", "profile");
        return "/views/mypage/profile.jsp";
    }
}
