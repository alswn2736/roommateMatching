package controller.feed;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dao.PostManager;
import model.dto.PostDTO;
import util.SessionUtil;

public class FeedController implements Controller {
    
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        PostManager manager = PostManager.getInstance();
        
        //feed 필터 조건
        String keyword = request.getParameter("keyword");
        String[] smoking = request.getParameterValues("smoking");
        String[] sleepTime = request.getParameterValues("sleepTime");
        String[] sensitivity = request.getParameterValues("sensitivity");
        String airCon = request.getParameter("airCon");
        String clean = request.getParameter("clean");
        String sort = request.getParameter("sort");
        
        // 필터 조건에 맞는 게시글 조회
        List<PostDTO> postList = manager.findPostList(keyword, smoking, sleepTime, sensitivity, airCon, clean, sort);
        request.setAttribute("postList", postList);
        request.setAttribute("postCount", postList.size());
        
        // 로그인한 사용자 학번
        Integer loginStudentId = SessionUtil.getLoginStudentId(request);
        request.setAttribute("loginStudentId", loginStudentId);
        
        return "/views/feed/feed.jsp";
    }
}
