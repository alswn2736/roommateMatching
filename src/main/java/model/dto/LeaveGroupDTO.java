package model.dto;

public class LeaveGroupDTO {
    private int leaveId;
    private int groupId;
    private int requesterId;
    private String reason;
    private String status;

    public LeaveGroupDTO() {}
    
    public LeaveGroupDTO(int leaveId, int groupId, int requesterId, String reason, String status) {
        super();
        this.leaveId = leaveId;
        this.groupId = groupId;
        this.requesterId = requesterId;
        this.reason = reason;
        this.status = status;
    }
    
    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}