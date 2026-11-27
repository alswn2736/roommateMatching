package model.dto.mypage;

import java.util.ArrayList;
import java.util.List;

import model.dto.GroupDTO;

/** 매칭 정보 화면: 내가 속한 그룹 1개 + 멤버 목록 */
public class MyGroupDTO {

    private GroupDTO group;
    private List<ProfileView> members = new ArrayList<ProfileView>();
    private String mateNames;      // 나를 제외한 멤버 이름 (쉼표 구분)
    private boolean leavePending;  // 내가 보낸 탈퇴 요청이 대기중인지

    public boolean isActive() {
        return group != null && GroupDTO.ACTIVE.equalsIgnoreCase(group.getStatus());
    }

    public String getTermLabel() {
        if (group == null || group.getAcademicYear() == 0) return "학기 미정";
        return group.getAcademicYear() + "학년도 " + group.getSemester() + "학기";
    }

    public String getRoomLabel() {
        int cap = group == null ? 0 : group.getCapacity();
        return cap > 0 ? cap + "인실 기숙사" : "기숙사";
    }

    public GroupDTO getGroup() { return group; }
    public void setGroup(GroupDTO group) { this.group = group; }

    public List<ProfileView> getMembers() { return members; }
    public void setMembers(List<ProfileView> members) { this.members = members; }

    public String getMateNames() { return mateNames; }
    public void setMateNames(String mateNames) { this.mateNames = mateNames; }

    public boolean isLeavePending() { return leavePending; }
    public void setLeavePending(boolean leavePending) { this.leavePending = leavePending; }
}
