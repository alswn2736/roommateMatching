package model.dto;

public class RuleAgreementDTO {
    private int ruleId;
    private int groupId;
    private int memberId;
    
    public RuleAgreementDTO() {}
    
    public RuleAgreementDTO(int ruleId, int groupId, int memberId) {
        super();
        this.ruleId = ruleId;
        this.groupId = groupId;
        this.memberId = memberId;
    }
    
    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    
    @Override
    public String toString() {
        return "RuleAgreementDTO [ruleId=" + ruleId + ", groupId=" + groupId + ", memberId=" + memberId + "]";
    }
}
