package model.dto;

public class GroupMemberDTO {
    private int groupId;
    private int studentId;
    
    public GroupMemberDTO() {}
    
    public GroupMemberDTO(int groupId, int studentId) {
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    @Override
    public String toString() {
        return "GroupMemberDTO [groupId=" + groupId + ", studentId=" + studentId + "]";
    }
}
