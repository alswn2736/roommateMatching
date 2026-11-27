package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.MyPageException;
import util.SessionUtil;

/** POST /mypage/account/withdraw (confirmation=탈퇴합니다) */
public class WithdrawCommand extends MyPageCommand {

    static final String CONFIRM_TEXT = "탈퇴합니다";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String confirmation = request.getParameter("confirmation");
        if (confirmation == null || !CONFIRM_TEXT.equals(confirmation.trim())) {
            setFlash(request, "error", "'" + CONFIRM_TEXT + "'를 정확히 입력해야 탈퇴할 수 있습니다.");
            return "redirect:/mypage/account";
        }
        try {
            service.withdraw(loginId(request));
        } catch (MyPageException e) {
            setFlash(request, "error", e.getMessage());
            return "redirect:/mypage/account";
        }
        SessionUtil.logout(request);
        return "redirect:" + LOGIN_PAGE;
    }
}
