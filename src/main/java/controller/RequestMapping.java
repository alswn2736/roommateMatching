package controller;

import java.util.HashMap;
import java.util.Map;

import controller.mypage.AccountCommand;
import controller.mypage.ApplicantsCommand;
import controller.mypage.ApplyDecisionCommand;
import controller.mypage.ChatListCommand;
import controller.mypage.ChatSendCommand;
import controller.mypage.LeaveRequestCommand;
import controller.mypage.LogoutCommand;
import controller.mypage.MatchedCommand;
import controller.mypage.PasswordCommand;
import controller.mypage.ProfileCommand;
import controller.mypage.ProfileImgCommand;
import controller.mypage.VisibilityCommand;
import controller.mypage.WithdrawCommand;

/**
 * "HTTP메서드 URI" → Command 매핑 테이블.
 * URI 는 컨텍스트 경로를 뺀 값 (예: "GET /mypage/profile").
 * 다른 기능(feed, group ...)도 여기에 한 줄씩 추가하고 FrontController 의 urlPatterns 에 경로를 추가하면 된다.
 */
public class RequestMapping {

    private final Map<String, Command> mappings = new HashMap<String, Command>();

    public RequestMapping() {
        // ===== 마이페이지 - 프로필 관리 =====
        mappings.put("GET /mypage/profile", new ProfileCommand());
        mappings.put("POST /mypage/profile/visibility", new VisibilityCommand());
        mappings.put("POST /mypage/profile/image", new ProfileImgCommand());

        // ===== 마이페이지 - 신청자 관리 / 채팅 =====
        mappings.put("GET /mypage/applicants", new ApplicantsCommand());
        mappings.put("POST /mypage/applicants/accept", new ApplyDecisionCommand(true));
        mappings.put("POST /mypage/applicants/reject", new ApplyDecisionCommand(false));
        mappings.put("GET /mypage/chat/list", new ChatListCommand());
        mappings.put("POST /mypage/chat/send", new ChatSendCommand());

        // ===== 마이페이지 - 매칭 정보 =====
        mappings.put("GET /mypage/matched", new MatchedCommand());
        mappings.put("POST /mypage/matched/leave", new LeaveRequestCommand());

        // ===== 마이페이지 - 계정 설정 =====
        mappings.put("GET /mypage/account", new AccountCommand());
        mappings.put("POST /mypage/account/password", new PasswordCommand());
        mappings.put("POST /mypage/account/logout", new LogoutCommand());
        mappings.put("POST /mypage/account/withdraw", new WithdrawCommand());
    }

    public Command find(String method, String uri) {
        return mappings.get(method + " " + uri);
    }

    /** 메서드만 다른 매핑이 있는지 (405 응답용) */
    public boolean hasUri(String uri) {
        for (String key : mappings.keySet()) {
            if (key.endsWith(" " + uri)) return true;
        }
        return false;
    }
}
