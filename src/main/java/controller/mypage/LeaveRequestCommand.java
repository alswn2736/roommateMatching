package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.MyPageException;

/** POST /mypage/matched/leave (groupId, reason) → JSON */
public class LeaveRequestCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int groupId = intParam(request, "groupId", 0);
        if (groupId <= 0) {
            return json(response, false, "잘못된 요청입니다.");
        }
        try {
            service.requestLeave(loginId(request), groupId, request.getParameter("reason"));
            return json(response, true, "탈퇴 요청을 보냈습니다. 나를 제외한 룸메이트 전원이 동의하면 진행됩니다.");
        } catch (MyPageException e) {
            return json(response, false, e.getMessage());
        }
    }
}
