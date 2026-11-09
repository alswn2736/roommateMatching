package model.dto;

public class RoleDTO {
    private int roleId;
    private int groupId;
    private String roleTitle;
    private int assigneeId;
    
    public RoleDTO() {}
    
    public RoleDTO(int roleId, int groupId, String roleTitle, int assigneeId) {
        super();
        this.roleId = roleId;
        this.groupId = groupId;
        this.roleTitle = roleTitle;
        this.assigneeId = assigneeId;
    }
    
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public int getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }
    
    @Override
    public String toString() {
        return "RoleDTO [roleId=" + roleId + ", groupId=" + groupId + ", roleTitle=" + roleTitle + ", assigneeId="
                + assigneeId + "]";
    }
}
