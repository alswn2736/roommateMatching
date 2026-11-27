package controller.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.ChatDTO;
import model.service.MyPageException;

/** GET /mypage/chat/list?applyId=1&after=0 → after 보다 새 메시지만 JSON 으로 (Ajax 폴링용) */
public class ChatListCommand extends MyPageCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int me = loginId(request);
        int applyId = intParam(request, "applyId", 0);
        int after = intParam(request, "after", 0);
        try {
            List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
            for (ChatDTO c : service.getChats(me, applyId, after)) {
                messages.add(toMap(c, me));
            }
            Map<String, Object> extra = new HashMap<String, Object>();
            extra.put("messages", messages);
            return json(response, true, null, extra);
        } catch (MyPageException e) {
            return json(response, false, e.getMessage());
        }
    }

    static Map<String, Object> toMap(ChatDTO c, int me) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("msgId", c.getMsgId());
        m.put("content", c.getContent());
        m.put("mine", c.getWriterId() == me);
        m.put("sentAt", c.getSentAt() == null ? null : c.getSentAt().getTime());
        return m;
    }
}
