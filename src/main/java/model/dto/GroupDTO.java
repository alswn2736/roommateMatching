package model.dto;

public class GroupDTO {
    private int groupId;
    private int capacity;
    private int academicYear;
    private int semester;
    private String status;

    public static final String ACTIVE = "ACTIVE";
    public static final String ENDED = "ENDED";

    public GroupDTO() {
    }

    public GroupDTO(int groupId, int capacity, int academicYear, int semester, String status) {
        this.groupId = groupId;
        this.capacity = capacity;
        this.academicYear = academicYear;
        this.semester = semester;
        this.status = status;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "GroupDTO [groupId=" + groupId + ", capacity=" + capacity + ", academicYear=" + academicYear
                + ", semester=" + semester + ", status=" + status + "]";
    }
}
