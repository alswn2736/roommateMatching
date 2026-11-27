package controller.mypage;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.ChatDTO;
import model.service.MyPageException;

/** POST /mypage/chat/send (applyId, content) → 저장된 메시지 JSON */
public class ChatSendCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int me = loginId(request);
        int applyId = intParam(request, "applyId", 0);
        try {
            ChatDTO chat = service.sendChat(me, applyId, request.getParameter("content"));
            Map<String, Object> extra = new HashMap<String, Object>();
            extra.put("chat", ChatListCommand.toMap(chat, me));
            return json(response, true, null, extra);
        } catch (MyPageException e) {
            return json(response, false, e.getMessage());
        }
    }
}
