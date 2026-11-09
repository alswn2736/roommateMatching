package model.dto;

public class RuleDTO {
    private int ruleId;
    private int groupId;
    private String content;
    
    public RuleDTO() {}
    
    public RuleDTO(int ruleId, int groupId, String content) {
        super();
        this.ruleId = ruleId;
        this.groupId = groupId;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return "RuleDTO [ruleId=" + ruleId + ", groupId=" + groupId + ", content=" + content + "]";
    }
}
