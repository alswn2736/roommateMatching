package model.domain;

import model.dto.UserDTO;

//회원 정보 관리 Domain 클래스
public class User {
	private int studentId;
    private String nickname;
    private String name;
    private String email;
    private String major;
    private String profileImg;
    private boolean namePublic;
    private boolean studentIdPublic;
    private boolean majorPublic;

    public User(UserDTO dto) {
        this.studentId = dto.getStudentId();
        this.nickname = dto.getNickname();
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.major = dto.getMajor();
        this.profileImg = dto.getProfileImg();
        this.namePublic = "Y".equals(dto.getNamePublic());
        this.studentIdPublic = "Y".equals(dto.getStudentIdPublic());
        this.majorPublic = "Y".equals(dto.getMajorPublic());
    }

    public boolean isProfileCompleted() {
        return nickname != null && !nickname.isBlank();
    }

    public int getStudentId() {
        return studentId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMajor() {
        return major;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public boolean isNamePublic() {
        return namePublic;
    }

    public boolean isStudentIdPublic() {
        return studentIdPublic;
    }

    public boolean isMajorPublic() {
        return majorPublic;
    }
}
