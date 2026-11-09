package model.dto;

public class UserDTO {
    private int studentId;          // 학번
    private String password;        // 비밀번호
    private String nickname;        // 닉네임
    private String name;            // 이름
    private String email;           // 학교 메일
    private String major;           // 전공
    private String profileImg;      // 프로필 사진
    private String namePublic;      // 이름 공개
    private String studentIdPublic; // 학번 공개
    private String majorPublic;     // 학과 공개

    public UserDTO() {}

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    @Override
	public String toString() {
		return "UserDTO [studentid=" + studentId + ", nickname=" + nickname + ", name=" + name + ", email=" + email
				+ ", major=" + major + "]";
	}
}
