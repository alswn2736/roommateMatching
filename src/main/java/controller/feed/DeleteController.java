package controller.feed;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dao.PostManager;
import util.SessionUtil;

public class DeleteController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        if (request.getMethod().equalsIgnoreCase("POST")) {
            int postId = Integer.parseInt(request.getParameter("postId"));
            
            // 임시ID (로그인 기능 연결 전)
            int writerId = 20260001;

            PostManager manager = PostManager.getInstance();
            manager.deletePost(postId, writerId);
        }

        return "redirect:/app/feed";
    }
}