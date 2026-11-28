package model.dao.group;

import java.util.*;
import model.dto.*;
import model.group.GroupMemberView;
import org.apache.ibatis.session.SqlSession;
public class GroupQueryDAO {

    private final SqlSession session;

    public GroupQueryDAO(SqlSession session) {
        this.session = session;
    }

    private Map<String, Object> args(int groupId) {
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("groupId", groupId);
        return p;
    }

    public GroupDTO lockGroup(int groupId) {
        return session.selectOne("group.lockGroup", args(groupId));
    }

    public List<GroupDTO> listGroups(int memberId) {
        Map<String, Object> p = args(0);
        p.put("memberId", memberId);
        return session.selectList("group.listGroups", p);
    }

    public List<GroupMemberView> members(int groupId) {
        return session.selectList("group.members", args(groupId));
    }

    public boolean isMember(int groupId, int memberId) {
        Map<String, Object> p = args(groupId);
        p.put("memberId", memberId);
        return (Integer)session.selectOne("group.memberCount", p)>0;
    }

    public boolean hasActiveGroup(int memberId, int year, int semester) {
        Map<String, Object> p = args(0);
        p.put("memberId", memberId);
        p.put("year", year);
        p.put("semester", semester);
        return (Integer)session.selectOne("group.hasActiveGroup", p)>0;
    }

    public String title(int groupId) {
        return session.selectOne("group.title", args(groupId));
    }

    public boolean hasPendingLeave(int groupId) {
        return (Integer)session.selectOne("group.pendingCount", args(groupId))>0;
    }

    public LeaveGroupDTO leave(int groupId, int leaveId) {
        Map<String, Object> p = args(groupId);
        p.put("leaveId", leaveId);
        return session.selectOne("group.leaveRequest", p);
    }

    public int nextRoleId() {
        return session.selectOne("group.nextRoleId");
    }

    public int nextRuleId() {
        return session.selectOne("group.nextRuleId");
    }

    public int nextLeaveId() {
        return session.selectOne("group.nextLeaveId");
    }

    public void clearRuleAgreements(int groupId, int ruleId) {
        Map<String, Object> p = args(groupId);
        p.put("ruleId", ruleId);
        session.delete("group.clearRuleAgreements", p);
    }

    public void removeMemberRelations(int groupId, int memberId, int successorId) {
        Map<String, Object> p = args(groupId);
        p.put("memberId", memberId);
        p.put("successorId", successorId);
        session.update("group.reassignRoles", p);
        session.delete("group.clearMemberAgreements", p);
        session.delete("group.clearMemberConsents", p);
    }
}
