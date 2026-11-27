package controller.mypage;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.mypage.MyGroupDTO;

/** GET /mypage/matched : 현재 그룹(ACTIVE) + 지난 그룹 목록 */
public class MatchedCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (loadMe(request) == null) {
            return "redirect:" + LOGIN_PAGE;
        }
        List<MyGroupDTO> active = new ArrayList<MyGroupDTO>();
        List<MyGroupDTO> past = new ArrayList<MyGroupDTO>();
        for (MyGroupDTO g : service.getMyGroups(loginId(request))) {
            if (g.isActive()) {
                active.add(g);
            } else {
                past.add(g);
            }
        }
        request.setAttribute("activeGroups", active);
        request.setAttribute("pastGroups", past);
        request.setAttribute("menu", "matched");
        return "/views/mypage/matched.jsp";
    }
}
