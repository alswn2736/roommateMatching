package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** GET /mypage/account : 계정 설정 화면 */
public class AccountCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (loadMe(request) == null) {
            return "redirect:" + LOGIN_PAGE;
        }
        moveFlashToRequest(request);
        request.setAttribute("menu", "account");
        return "/views/mypage/account.jsp";
    }
}
