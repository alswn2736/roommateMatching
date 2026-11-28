package model.group;

import java.util.*;
import model.dto.*;
public class GroupPage {

    private GroupDTO group;

    private String title;

    private List<GroupMemberView> members;

    private List<RoleDTO> roles;

    private List<RuleDTO> rules;

    private Map<Integer, Integer> agreementCounts;

    private Set<Integer> agreedRuleIds;

    private List<LeaveGroupDTO> leaveRequests;

    private Map<Integer, List<LeaveConsentDTO>> consents;

    private Set<Integer> answerableLeaves;

    private Map<Integer, String> memberNames;

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GroupMemberView> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberView> members) {
        this.members = members;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<RuleDTO> rules) {
        this.rules = rules;
    }

    public Map<Integer, Integer> getAgreementCounts() {
        return agreementCounts;
    }

    public void setAgreementCounts(Map<Integer, Integer> agreementCounts) {
        this.agreementCounts = agreementCounts;
    }

    public Set<Integer> getAgreedRuleIds() {
        return agreedRuleIds;
    }

    public void setAgreedRuleIds(Set<Integer> agreedRuleIds) {
        this.agreedRuleIds = agreedRuleIds;
    }

    public List<LeaveGroupDTO> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(List<LeaveGroupDTO> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    public Map<Integer, List<LeaveConsentDTO>> getConsents() {
        return consents;
    }

    public void setConsents(Map<Integer, List<LeaveConsentDTO>> consents) {
        this.consents = consents;
    }

    public Set<Integer> getAnswerableLeaves() {
        return answerableLeaves;
    }

    public void setAnswerableLeaves(Set<Integer> answerableLeaves) {
        this.answerableLeaves = answerableLeaves;
    }

    public Map<Integer, String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(Map<Integer, String> memberNames) {
        this.memberNames = memberNames;
    }
}
