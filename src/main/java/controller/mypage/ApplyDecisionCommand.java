package controller.mypage;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.MyPageException;

/** POST /mypage/applicants/accept , /mypage/applicants/reject (applyId) → JSON */
public class ApplyDecisionCommand extends MyPageCommand {

    private final boolean accept;

    public ApplyDecisionCommand(boolean accept) {
        this.accept = accept;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int applyId = intParam(request, "applyId", 0);
        if (applyId <= 0) {
            return json(response, false, "잘못된 요청입니다.");
        }
        try {
            if (accept) {
                boolean groupCreated = service.acceptApply(loginId(request), applyId);
                Map<String, Object> extra = new HashMap<String, Object>();
                extra.put("groupCreated", groupCreated);
                return json(response, true, groupCreated
                        ? "모집 인원이 모두 찼습니다! 룸메이트 그룹이 만들어졌어요."
                        : "신청을 수락했습니다.", extra);
            }
            service.rejectApply(loginId(request), applyId);
            return json(response, true, "신청을 거절했습니다.");
        } catch (MyPageException e) {
            return json(response, false, e.getMessage());
        }
    }
}
