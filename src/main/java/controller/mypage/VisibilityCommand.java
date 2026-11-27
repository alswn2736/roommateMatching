package controller.mypage;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.MyPageException;

/** POST /mypage/profile/visibility (field=namePublic|studentIdPublic|majorPublic, value=Y|N) → JSON */
public class VisibilityCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String field = request.getParameter("field");
        boolean open = "Y".equalsIgnoreCase(request.getParameter("value"));
        try {
            service.updateVisibility(loginId(request), field, open);
        } catch (MyPageException e) {
            return json(response, false, e.getMessage());
        }
        Map<String, Object> extra = new HashMap<String, Object>();
        extra.put("field", field);
        extra.put("value", open ? "Y" : "N");
        return json(response, true, open ? "공개로 변경되었습니다." : "비공개로 변경되었습니다.", extra);
    }
}
