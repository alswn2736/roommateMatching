package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.SessionUtil;

/** POST /mypage/account/logout */
public class LogoutCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionUtil.logout(request);
        return "redirect:" + LOGIN_PAGE;
    }
}
