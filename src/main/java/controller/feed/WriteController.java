
package controller.feed;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Controller;
import model.dao.PostManager;
import model.dto.PostDTO;

public class WriteController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        request.setCharacterEncoding("UTF-8");

        if (request.getMethod().equalsIgnoreCase("POST")) {
            
            PostDTO post = new PostDTO();
            
            // 임시ID (로그인 기능 연결 전)
            post.setWriterId(20260001);
            
            post.setTitle(request.getParameter("title"));
            String roomType = request.getParameter("roomType");
            
            post.setRoomType(roomType);
            if ("DOUBLE".equals(roomType)) {
                post.setCapacity(1);
            } else if ("QUAD".equals(roomType)) {
                post.setCapacity(3);
            }
            
            // 생활 습관 기본값
            post.setKwSmoking(null);
            post.setKwSleepTime(null);
            post.setKwSensitivity(null);
            post.setKwAirCon(null);
            post.setKwClean(null);
            
            // 선택한 생활 습관
            String[] habits = request.getParameterValues("habits");
            if (habits != null) {
                for (String habit : habits) {
                    
                    if ("아침형".equals(habit)) {
                        post.setKwSleepTime("M");
                    } else if ("저녁형".equals(habit)) {
                        post.setKwSleepTime("E");
                    } else if ("비흡연자".equals(habit)) {
                        post.setKwSmoking("N");
                    } else if ("흡연 가능".equals(habit)) {
                        post.setKwSmoking("Y");
                    } else if ("조용한 편".equals(habit)) {
                        post.setKwSensitivity("Q");
                    } else if ("사교적".equals(habit)) {
                        post.setKwSensitivity("S");
                    } else if ("에어컨 빵빵".equals(habit)) {
                        post.setKwAirCon("Y");
                    } else if ("위생청결 철저".equals(habit)) {
                        post.setKwClean("Y");
                    }
                }
            }
            
            post.setContent(request.getParameter("description"));
            
            PostManager manager = PostManager.getInstance();
            manager.createPost(post);
        }
        
        return "redirect:/app/feed";
    }
}