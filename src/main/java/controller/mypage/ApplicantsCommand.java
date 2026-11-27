package controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** GET /mypage/applicants : 내 모집글별 신청자 관리 화면 */
public class ApplicantsCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (loadMe(request) == null) {
            return "redirect:" + LOGIN_PAGE;
        }
        request.setAttribute("myPosts", service.getMyPosts(loginId(request)));
        request.setAttribute("menu", "applicants");
        return "/views/mypage/applicants.jsp";
    }
}
