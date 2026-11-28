package controller.group;

import java.io.IOException;
import java.sql.SQLException;
import util.GroupCsrf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dto.UserDTO;
import model.domain.User;
import model.group.*;
import model.service.group.GroupService;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
@WebServlet(urlPatterns = {"/app/group","/app/group/list","/app/matched","/app/group/role","/app/group/rule","/app/group/rule/agree","/app/group/leave","/app/group/leave/consent"})
public class GroupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GroupService service;

    private SqlSessionFactory ownedFactory;

    @Override
    public void init() {
        Object shared = getServletContext().getAttribute("group.service");
        if (shared instanceof GroupService) {
            service = (GroupService)shared;
        }
        else {
            try {
                ownedFactory = GroupDatabase.fromEnvironment();
                service = new GroupService(ownedFactory);
            }
            catch (RuntimeException e) {
                getServletContext().log("그룹 DB 설정이 준비되지 않았습니다.", e);
            }
        }
    }

    @Override
    public void destroy() {
        if (ownedFactory != null && ownedFactory.getConfiguration().getEnvironment().getDataSource() instanceof PooledDataSource) {
            PooledDataSource dataSource = (PooledDataSource) ownedFactory.getConfiguration().getEnvironment().getDataSource();
            dataSource.forceCloseAll();
        }
    }

    private int login(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        String name = getServletContext().getInitParameter("group.loginAttribute");
        if (name == null) {
            name = "loginMember";
        }
        Object user = null;
        if (session != null) {
            user = session.getAttribute(name);
        }
        int studentId = 0;
        if (user instanceof User) {
            studentId = ((User) user).getStudentId();
        } else if (user instanceof UserDTO) {
            studentId = ((UserDTO) user).getStudentId();
        }
        if (studentId <= 0) {
            throw new GroupException(401,"로그인 후 이용해 주세요.");
        }
        return studentId;
    }

    private String csrf(HttpSession session) {
        return GroupCsrf.token(session);
    }

    private void checkCsrf(HttpServletRequest req) {
        if (!GroupCsrf.isValid(req)) {
            throw new GroupException(403, "요청 확인에 실패했습니다. 화면을 새로고침해 주세요.");
        }
    }

    private int number(HttpServletRequest req, String name, boolean optional) {
        String value = req.getParameter(name);
        if (optional && (value == null || value.trim().isEmpty())) {
            return 0;
        }
        try {
            int n = Integer.parseInt(value);
            if (n<=0) {
                throw new NumberFormatException();
            }
            return n;
        }
        catch (RuntimeException e) {
            throw new GroupException(400,"잘못된 " + name + " 값입니다.");
        }
    }

    private void ready() {
        if (service == null) {
            throw new GroupException(503,"그룹 DB 연결 설정을 확인해 주세요.");
        }
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, int status, String message) throws ServletException, IOException {
        resp.setStatus(status);
        req.setAttribute("groupError", message);
        req.getRequestDispatcher("/WEB-INF/views/group/error.jsp").forward(req, resp);
    }

    private void headers(HttpServletResponse resp) {
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control","no-store");
        resp.setHeader("X-Content-Type-Options","nosniff");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        headers(resp);
        try {
            int memberId = login(req);
            ready();
            String path = req.getServletPath();
            if (!path.equals("/app/group") && !path.equals("/app/group/list") && !path.equals("/app/matched")) {
                resp.setHeader("Allow","POST");
                throw new GroupException(405,"이 작업은 폼으로 제출해 주세요.");
            }
            req.setAttribute("csrf", csrf(req.getSession()));
            req.setAttribute("currentMemberId", memberId);
            String message = (String)req.getSession().getAttribute("group.message");
            req.getSession().removeAttribute("group.message");
            req.setAttribute("groupMessage", message);
            if (!path.equals("/app/group") || req.getParameter("groupId") == null) {
                req.setAttribute("groupList", service.getInGroups(memberId));
                req.getRequestDispatcher("/WEB-INF/views/group/list.jsp").forward(req, resp);
            }
            else {
                req.setAttribute("groupPage", service.getDetail(number(req,"groupId", false), memberId));
                req.getRequestDispatcher("/WEB-INF/views/group/detail.jsp").forward(req, resp);
            }
        }
        catch (GroupException e) {
            error(req, resp, e.getStatus(), e.getMessage());
        }
        catch (SQLException|RuntimeException e) {
            getServletContext().log("그룹 조회 실패", e);
            error(req, resp, 500,"조회에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    private void formError(HttpServletRequest req, HttpServletResponse resp, GroupException failure) throws ServletException, IOException {
        if (failure.getStatus() != 400 && failure.getStatus() != 409) {
            error(req, resp, failure.getStatus(), failure.getMessage());
            return;
        }
        try {
            int groupId = number(req,"groupId", false);
            int memberId = login(req);
            req.setAttribute("groupPage", service.getDetail(groupId, memberId));
            req.setAttribute("csrf", csrf(req.getSession()));
            req.setAttribute("currentMemberId", memberId);
            req.setAttribute("formError", failure.getMessage());
            String path = req.getServletPath();
            String dialog = null;
            if (path.equals("/app/group/role")) {
                int id = number(req,"roleId", true);
                if (id == 0) {
                    dialog = "role-new";
                } else {
                    dialog = "role-" + id;
                }
            }
            else if (path.equals("/app/group/rule")) {
                int id = number(req,"ruleId", true);
                if (id == 0) {
                    dialog = "rule-new";
                } else {
                    dialog = "rule-" + id;
                }
            }
            else if (path.equals("/app/group/leave")) {
                dialog = "leave-dialog";
            }
            else if (path.equals("/app/group/leave/consent")) {
                dialog = "consent-"+number(req,"leaveId", false);
            }
            req.setAttribute("reopenDialog", dialog);
            resp.setStatus(failure.getStatus());
            req.getRequestDispatcher("/WEB-INF/views/group/detail.jsp").forward(req, resp);
        }
        catch (SQLException|RuntimeException e) {
            error(req, resp, failure.getStatus(), failure.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        headers(resp);
        try {
            int memberId = login(req);
            checkCsrf(req);
            ready();
            int groupId = number(req,"groupId", false);
            String action = req.getParameter("action");
            if (action == null) {
                action = "save";
            }
            switch (req.getServletPath()) {
                case "/app/group/role":
                if ("delete".equals(action)) {
                    service.deleteRole(groupId, memberId, number(req,"roleId", false));
                }
                else if ("save".equals(action)) {
                    service.saveRole(groupId, memberId, number(req,"roleId", true), req.getParameter("roleTitle"), number(req,"assigneeId", false));
                }
                else {
                    throw new GroupException(400,"잘못된 작업입니다.");
                }
                break;
                case "/app/group/rule":
                if ("delete".equals(action)) {
                    service.deleteRule(groupId, memberId, number(req,"ruleId", false));
                }
                else if ("save".equals(action)) {
                    service.saveRule(groupId, memberId, number(req,"ruleId", true), req.getParameter("content"));
                }
                else {
                    throw new GroupException(400,"잘못된 작업입니다.");
                }
                break;
                case "/app/group/rule/agree": service.agreeRule(groupId, memberId, number(req,"ruleId", false));
                break;
                case "/app/group/leave": service.requestLeave(groupId, memberId, req.getParameter("reason"));
                break;
                case "/app/group/leave/consent": service.answerLeave(groupId, memberId, number(req,"leaveId", false), req.getParameter("agreed"));
                break;
                default: resp.setHeader("Allow","GET");
                throw new GroupException(405,"지원하지 않는 요청입니다.");
            }
            req.getSession().setAttribute("group.message","저장되었습니다.");
            String section = "leave";
            if (req.getServletPath().contains("/role")) {
                section = "roles";
            }
            else if (req.getServletPath().contains("/rule")) {
                section = "rules";
            }
            resp.setStatus(303);
            resp.setHeader("Location", resp.encodeRedirectURL(req.getContextPath()+"/app/group?groupId="+groupId+"#"+section));
        }
        catch (GroupException e) {
            formError(req, resp, e);
        }
        catch (SQLException|RuntimeException e) {
            getServletContext().log("그룹 변경 실패", e);
            error(req, resp, 500,"저장하지 못했습니다. 변경 사항은 취소되었습니다.");
        }
    }
}
