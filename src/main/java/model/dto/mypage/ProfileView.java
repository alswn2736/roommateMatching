package model.dto.mypage;

import model.dto.UserDTO;

/**
 * 화면 표시용 사용자 정보 (비밀번호 제외).
 * 공개 여부(Y/N)에 따라 다른 학생에게 보여줄 값을 계산해 주는 getter 를 가진다.
 */
public class ProfileView {

    public static final String DEFAULT_IMG = "/assets/image/mypage/Avatar.png";

    private int studentId;
    private String nickname;
    private String name;
    private String email;
    private String major;
    private String profileImg;
    private String namePublic;
    private String studentIdPublic;
    private String majorPublic;

    public ProfileView() {}

    public static ProfileView from(UserDTO u) {
        ProfileView p = new ProfileView();
        p.setStudentId(u.getStudentId());
        p.setNickname(u.getNickname());
        p.setName(u.getName());
        p.setEmail(u.getEmail());
        p.setMajor(u.getMajor());
        p.setProfileImg(u.getProfileImg());
        p.setNamePublic(u.getNamePublic());
        p.setStudentIdPublic(u.getStudentIdPublic());
        p.setMajorPublic(u.getMajorPublic());
        return p;
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    // ===== 공개 여부 =====
    public boolean isNameOpen() { return "Y".equalsIgnoreCase(namePublic); }
    public boolean isStudentIdOpen() { return "Y".equalsIgnoreCase(studentIdPublic); }
    public boolean isMajorOpen() { return "Y".equalsIgnoreCase(majorPublic); }

    // ===== 본인 화면용 =====
    /** 본인 이름 (없으면 닉네임) */
    public String getMyName() {
        if (hasText(name)) return name;
        if (hasText(nickname)) return nickname;
        return "이름 미등록";
    }

    public String getMyMajor() {
        return hasText(major) ? major : "학과 미등록";
    }

    /** 20231234 → "23학번" */
    public String getYearLabel() {
        String s = String.valueOf(studentId);
        if (studentId <= 0) return "-";
        if (s.length() >= 8) return s.substring(2, 4) + "학번";
        if (s.length() >= 2) return s.substring(0, 2) + "학번";
        return s + "학번";
    }

    // ===== 다른 학생에게 보이는 값 (공개 범위 반영) =====
    public String getDisplayName() {
        if (isNameOpen() && hasText(name)) return name;
        if (hasText(nickname)) return nickname;
        return "익명";
    }

    public String getDisplayMajor() {
        return isMajorOpen() && hasText(major) ? major : "학과 비공개";
    }

    public String getDisplayYear() {
        return isStudentIdOpen() ? getYearLabel() : "학번 비공개";
    }

    public String getBio() {
        return getDisplayMajor() + " • " + getDisplayYear();
    }

    /** 컨텍스트 경로 뒤에 붙일 이미지 경로 ("/assets/...") */
    public String getProfileImgPath() {
        if (!hasText(profileImg)) return DEFAULT_IMG;
        return profileImg.startsWith("/") ? profileImg : "/" + profileImg;
    }

    // ===== getter / setter =====
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getProfileImg() { return profileImg; }
    public void setProfileImg(String profileImg) { this.profileImg = profileImg; }

    public String getNamePublic() { return namePublic; }
    public void setNamePublic(String namePublic) { this.namePublic = namePublic; }

    public String getStudentIdPublic() { return studentIdPublic; }
    public void setStudentIdPublic(String studentIdPublic) { this.studentIdPublic = studentIdPublic; }

    public String getMajorPublic() { return majorPublic; }
    public void setMajorPublic(String majorPublic) { this.majorPublic = majorPublic; }
}
