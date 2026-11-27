package model.service.group;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import model.dao.*;
import model.dao.group.GroupQueryDAO;
import model.dto.*;
import model.group.*;
import org.apache.ibatis.session.SqlSessionFactory;

public class GroupService {

    private final SqlSessionFactory factory;

    public GroupService(SqlSessionFactory factory) {
        this.factory = factory;
    }

    private GroupDTO authorize(GroupQueryDAO query, int groupId, int memberId, boolean write) {
        GroupDTO group = query.lockGroup(groupId);
        if (group == null) {
            throw new GroupException(404, "그룹을 찾을 수 없습니다.");
        }
        if (!query.isMember(groupId, memberId)) {
            throw new GroupException(403, "그룹 구성원만 이용할 수 있습니다.");
        }
        if (write && !GroupDTO.ACTIVE.equals(group.getStatus())) {
            throw new GroupException(409, "종료된 그룹은 변경할 수 없습니다.");
        }
        return group;
    }

    private void requireOneRow(int affected) {
        if (affected != 1) {
            throw new GroupException(409, "데이터가 변경되었습니다. 새로고침 후 다시 시도해 주세요.");
        }
    }

    private void requireMember(GroupQueryDAO query, int groupId, int studentId) {
        if (!query.isMember(groupId, studentId)) {
            throw new GroupException(400, "현재 그룹 구성원을 선택해 주세요.");
        }
    }

    public List<GroupDTO> getInGroups(int memberId) {
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            return new GroupQueryDAO(tx.getSession()).listGroups(memberId);
        }
    }

    public boolean hasActiveGroup(int memberId, int year, int semester) {
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            return new GroupQueryDAO(tx.getSession()).hasActiveGroup(memberId, year, semester);
        }
    }

    public GroupPage getDetail(int groupId, int memberId) throws SQLException {
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            Connection conn = tx.getSession().getConnection();
            GroupPage page = new GroupPage();
            page.setGroup(authorize(query, groupId, memberId, false));
            page.setTitle(query.title(groupId));
            page.setMembers(query.members(groupId));
            Map<Integer, String> names = new HashMap<Integer, String>();
            for (GroupMemberView m : page.getMembers()) {
                names.put(m.getStudentId(), m.getNickname());
            }
            page.setMemberNames(names);
            page.setRoles(new RoleDAO().findByGroup(conn, (long) groupId));
            page.setRules(new RuleDAO().findByGroup(conn, (long) groupId));
            Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
            Set<Integer> mine = new HashSet<Integer>();
            for (RuleAgreementDTO a : new RuleAgreementDAO().findByGroup(conn, (long) groupId)) {
                if (names.containsKey(a.getMemberId())) {
                    Integer count = counts.get(a.getRuleId());
                    if (count == null) {
                        count = 0;
                    }
                    counts.put(a.getRuleId(), count + 1);
                }
                if (a.getMemberId() == memberId) {
                    mine.add(a.getRuleId());
                }
            }
            page.setAgreementCounts(counts);
            page.setAgreedRuleIds(mine);
            page.setLeaveRequests(new LeaveGroupDAO().findByGroup(conn, (long) groupId));
            Map<Integer, List<LeaveConsentDTO>> consents = new HashMap<Integer, List<LeaveConsentDTO>>();
            for (LeaveGroupDTO leave : page.getLeaveRequests()) {
                consents.put(leave.getLeaveId(), new ArrayList<LeaveConsentDTO>());
            }
            if (!page.getLeaveRequests().isEmpty()) {
                for (LeaveConsentDTO vote : new LeaveConsentDAO().findByGroup(conn, (long) groupId)) {
                    consents.get(vote.getLeaveId()).add(vote);
                }
            }
            Set<Integer> answerable = new HashSet<Integer>();
            for (LeaveGroupDTO leave : page.getLeaveRequests()) {
                List<LeaveConsentDTO> votes = consents.get(leave.getLeaveId());
                for (LeaveConsentDTO vote : votes) {
                    if (vote.getMemberId() == memberId && vote.getAgreed() == null && GroupPolicy.PENDING.equals(leave.getStatus())) {
                        answerable.add(leave.getLeaveId());
                    }
                }
            }
            page.setConsents(consents);
            page.setAnswerableLeaves(answerable);
            return page;
        }
    }

    public void saveRole(int groupId, int memberId, int roleId, String title, int assigneeId) throws SQLException {
        title = GroupPolicy.text(title, 100,"역할 내용");
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            authorize(query, groupId, memberId, true);
            requireMember(query, groupId, assigneeId);
            RoleDAO dao = new RoleDAO();
            Connection conn = tx.getSession().getConnection();
            if (roleId == 0) {
                requireOneRow(dao.insert(conn, new RoleDTO(query.nextRoleId(), groupId, title, assigneeId)));
            }
            else {
                requireOneRow(dao.update(conn, new RoleDTO(roleId, groupId, title, assigneeId)));
            }
            tx.commit();
        }
    }

    public void deleteRole(int groupId, int memberId, int roleId) throws SQLException {
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            authorize(new GroupQueryDAO(tx.getSession()), groupId, memberId, true);
            requireOneRow(new RoleDAO().delete(tx.getSession().getConnection(), (long) roleId, (long) groupId));
            tx.commit();
        }
    }

    public void saveRule(int groupId, int memberId, int ruleId, String content) throws SQLException {
        content = GroupPolicy.text(content, 100,"규칙");
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            authorize(query, groupId, memberId, true);
            Connection conn = tx.getSession().getConnection();
            RuleDAO dao = new RuleDAO();
            if (ruleId == 0) {
                requireOneRow(dao.insert(conn, new RuleDTO(query.nextRuleId(), groupId, content)));
            }
            else {
                RuleDTO current = null;
                for (RuleDTO rule : dao.findByGroup(conn, (long) groupId)) {
                    if (rule.getRuleId() == ruleId) {
                        current = rule;
                        break;
                    }
                }
                if (current == null) {
                    throw new GroupException(404, "규칙을 찾을 수 없습니다.");
                }
                if (content.equals(current.getContent())) {
                    tx.commit();
                    return;
                }
                requireOneRow(dao.update(conn, new RuleDTO(ruleId, groupId, content)));
                query.clearRuleAgreements(groupId, ruleId);
            }
            tx.commit();
        }
    }

    public void deleteRule(int groupId, int memberId, int ruleId) throws SQLException {
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            authorize(query, groupId, memberId, true);
            query.clearRuleAgreements(groupId, ruleId);
            requireOneRow(new RuleDAO().delete(tx.getSession().getConnection(), (long) ruleId, (long) groupId));
            tx.commit();
        }
    }

    public void agreeRule(int groupId, int memberId, int ruleId) throws SQLException {
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            authorize(query, groupId, memberId, true);
            Connection conn = tx.getSession().getConnection();
            boolean found = false;
            for (RuleDTO r:new RuleDAO().findByGroup(conn, (long) groupId)) {
                if (r.getRuleId() == ruleId) {
                    found = true;
                }
            }
            if (!found) {
                throw new GroupException(404,"규칙을 찾을 수 없습니다.");
            }
            RuleAgreementDAO dao = new RuleAgreementDAO();
            for (RuleAgreementDTO a:dao.findByRule(conn, (long) groupId, (long) ruleId)) {
                if (a.getMemberId() == memberId) {
                    tx.commit();
                    return;
                }
            }
            requireOneRow(dao.insert(conn, new RuleAgreementDTO(ruleId, groupId, memberId)));
            tx.commit();
        }
    }

    public int requestLeave(int groupId, int memberId, String reason) throws SQLException {
        if (reason == null) {
            reason = "";
        }
        if (reason.codePointCount(0, reason.length())>300) {
            throw new GroupException(400,"사유는 300자 이내로 입력해 주세요.");
        }
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            authorize(query, groupId, memberId, true);
            if (query.hasPendingLeave(groupId)) {
                throw new GroupException(409,"진행 중인 퇴실 요청을 먼저 처리해 주세요.");
            }
            List<GroupMemberView> members = query.members(groupId);
            if (members.size()<2) {
                throw new GroupException(409,"마지막 구성원의 퇴실은 관리자에게 문의해 주세요.");
            }
            int leaveId = query.nextLeaveId();
            Connection conn = tx.getSession().getConnection();
            requireOneRow(new LeaveGroupDAO().insert(conn, new LeaveGroupDTO(leaveId, groupId, memberId, reason, GroupPolicy.PENDING)));
            LeaveConsentDAO dao = new LeaveConsentDAO();
            for (GroupMemberView m : members) {
                if (m.getStudentId() != memberId) {
                    requireOneRow(dao.insert(conn, new LeaveConsentDTO(groupId, leaveId, m.getStudentId(), null)));
                }
            }
            tx.commit();
            return leaveId;
        }
    }

    public void answerLeave(int groupId, int memberId, int leaveId, String agreed) throws SQLException {
        agreed = GroupPolicy.yesNo(agreed);
        try (GroupTransaction tx = new GroupTransaction(factory)) {
            GroupQueryDAO query = new GroupQueryDAO(tx.getSession());
            authorize(query, groupId, memberId, true);
            LeaveGroupDTO request = query.leave(groupId, leaveId);
            if (request == null) {
                throw new GroupException(404,"퇴실 요청이 없습니다.");
            }
            if (!GroupPolicy.PENDING.equals(request.getStatus())) {
                throw new GroupException(409,"이미 처리된 요청입니다.");
            }
            if (request.getRequesterId() == memberId) {
                throw new GroupException(403,"본인 요청에는 응답할 수 없습니다.");
            }
            Connection conn = tx.getSession().getConnection();
            LeaveConsentDAO dao = new LeaveConsentDAO();
            List<LeaveConsentDTO> votes = dao.findByRequest(conn, (long) groupId, (long) leaveId);
            if ("Y".equals(agreed)) {
                List<GroupMemberView> currentMembers = query.members(groupId);
                if (currentMembers.size() != votes.size() + 1) {
                    throw new GroupException(409, "요청 이후 구성원이 변경되었습니다. 기존 요청을 거절한 후 다시 요청해 주세요.");
                }
                requireMember(query, groupId, request.getRequesterId());
                for (LeaveConsentDTO vote : votes) {
                    requireMember(query, groupId, vote.getMemberId());
                }
            }
            LeaveConsentDTO mine = null;
            for (LeaveConsentDTO vote:votes) {
                if (vote.getMemberId() == memberId) {
                    mine = vote;
                }
            }
            if (mine == null) {
                throw new GroupException(403,"이 요청의 응답자가 아닙니다.");
            }
            if (mine.getAgreed() != null) {
                throw new GroupException(409,"이미 응답했습니다.");
            }
            mine.setAgreed(agreed);
            requireOneRow(dao.update(conn, mine));
            boolean all = !votes.isEmpty(), rejected = false;
            for (LeaveConsentDTO vote:votes) {
                if (!"Y".equals(vote.getAgreed())) {
                    all = false;
                }
                if ("N".equals(vote.getAgreed())) {
                    rejected = true;
                }
            }
            if (rejected) {
                request.setStatus(GroupPolicy.REJECTED);
            }
            else if (all) {
                requireMember(query, groupId, request.getRequesterId());
                query.removeMemberRelations(groupId, request.getRequesterId(), memberId);
                requireOneRow(new GroupMemberDAO().delete(conn, (long) groupId, (long) request.getRequesterId()));
                request.setStatus(GroupPolicy.APPROVED);
            }
            if (rejected || all) {
                requireOneRow(new LeaveGroupDAO().update(conn, request));
            }
            tx.commit();
        }
    }

    public GroupDTO createGroup(Connection conn, int postId, int year, int semester) throws SQLException {
        if (conn.getAutoCommit()) {
            throw new IllegalArgumentException("그룹 생성은 신청 수락 트랜잭션 안에서 호출하세요.");
        }
        if (year<2000 || year>9999 || (semester != 1 && semester != 2)) {
            throw new GroupException(400,"학년도를 확인해 주세요.");
        }
        try (java.sql.PreparedStatement lock = conn.prepareStatement("SELECT writerid,capacity FROM post WHERE postid=? FOR UPDATE")) {
            lock.setInt(1, postId);
            try (java.sql.ResultSet rs = lock.executeQuery()) {
                if (!rs.next()) {
                    throw new GroupException(404,"모집글이 없습니다.");
                }
                int writer = rs.getInt(1), capacity = rs.getInt(2);
                List<GroupDTO> existing = new GroupDAO().findByGroup(conn, (long) postId);
                if (!existing.isEmpty()) {
                    return existing.get(0);
                }
                Set<Integer> members = new LinkedHashSet<Integer>();
                members.add(writer);
                for (ApplyDTO a:new ApplyDAO().selectByPost(conn, postId)) {
                    if (ApplyDTO.ACCEPTED.equals(a.getStatus())) {
                        members.add(a.getApplicantId());
                    }
                }
                if (capacity<2 || members.size() != capacity) {
                    throw new GroupException(409,"수락 인원이 정원과 일치해야 합니다.");
                }
                GroupDTO group = new GroupDTO(postId, capacity, year, semester, GroupDTO.ACTIVE);
                requireOneRow(new GroupDAO().insert(conn, group));
                for (Integer id:members) {
                    requireOneRow(new GroupMemberDAO().insert(conn, new GroupMemberDTO(postId, id)));
                }
                return group;
            }
        }
    }
}
