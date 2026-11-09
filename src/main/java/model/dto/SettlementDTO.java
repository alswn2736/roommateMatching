package model.dto;

public class SettlementDTO {
    private int groupId;
    private String month;
    private String settled;
    
    public SettlementDTO() {}
    
    public SettlementDTO(int groupId, String month, String settled) {
        super();
        this.groupId = groupId;
        this.month = month;
        this.settled = settled;
    }
    
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }
    
    @Override
    public String toString() {
        return "SettlementDTO [groupId=" + groupId + ", month=" + month + ", settled=" + settled + "]";
    }
}
