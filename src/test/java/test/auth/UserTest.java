package test.auth;

import model.domain.User;
import model.dto.UserDTO;

public class UserTest {

    public static void main(String[] args) {

        UserDTO dto = new UserDTO();

        dto.setStudentId(20201234);
        dto.setNamePublic("Y");
        dto.setStudentIdPublic("N");
        dto.setMajorPublic("Y");

        User user = new User(dto);

        System.out.println("학번: " + user.getStudentId());
        System.out.println("프로필 설정 완료: "
                + user.isProfileCompleted());

        dto.setNickname("테스트");
        user = new User(dto);

        System.out.println("프로필 설정 완료: "
                + user.isProfileCompleted());

        System.out.println("이름 공개: " + user.isNamePublic());
        System.out.println("학번 공개: " + user.isStudentIdPublic());
        System.out.println("학과 공개: " + user.isMajorPublic());
    }
}