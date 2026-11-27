package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.MyPageException;

/** POST /mypage/account/password (newPw, confirmNewPw) */
public class PasswordCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            service.changePassword(loginId(request),
                    request.getParameter("newPw"), request.getParameter("confirmNewPw"));
            setFlash(request, "success", "비밀번호가 변경되었습니다.");
        } catch (MyPageException e) {
            setFlash(request, "error", e.getMessage());
        }
        return "redirect:/mypage/account";
    }
}
