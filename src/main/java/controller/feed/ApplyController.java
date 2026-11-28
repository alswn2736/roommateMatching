
package controller.feed;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dao.ApplyManager;
import model.dto.ApplyDTO;

public class ApplyController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        if (request.getMethod().equalsIgnoreCase("POST")) {
            ApplyDTO apply = new ApplyDTO();

            // 임시ID (로그인 기능 연결 전)
            apply.setApplicantId(20260002);

            apply.setPostId(Integer.parseInt(request.getParameter("postId")));
            apply.setIntroduction(request.getParameter("introduction"));

            ApplyManager manager = ApplyManager.getInstance();

            try {
                manager.createApply(apply);
                request.getSession().setAttribute("applyMessage", "신청이 완료되었습니다.");
            } catch (IllegalArgumentException e) {
                request.getSession().setAttribute("applyMessage", e.getMessage());
            }
        }

        return "redirect:/app/feed";
    }
}
