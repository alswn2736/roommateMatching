package model.dto;

public class LeaveConsentDTO {
    private int groupId;
    private int leaveId;
    private int memberId;
    private String agreed;
    
    public LeaveConsentDTO() {}
    
    public LeaveConsentDTO(int groupId, int leaveId, int memberId, String agreed) {
        super();
        this.groupId = groupId;
        this.leaveId = leaveId;
        this.memberId = memberId;
        this.agreed = agreed;
    }
    
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAgreed() {
        return agreed;
    }

    public void setAgreed(String agreed) {
        this.agreed = agreed;
    }
    
    @Override
    public String toString() {
        return "LeaveConsentDTO [groupId=" + groupId + ", leaveId=" + leaveId + ", memberId=" + memberId + ", agreed="
                + agreed + "]";
    }
}
