package test.auth;

import model.domain.User;
import model.service.UserService;

public class UserServiceTest {

    public static void main(String[] args) throws Exception {

        UserService service = new UserService();

        try {
            service.signup("test@gmail.com",
                    "test1234", "test1234");

            System.out.println("이메일 검증 실패");

        } catch (IllegalArgumentException e) {
            System.out.println("이메일 검증 성공: " + e.getMessage());
        }

        try {
            service.signup("20201234@dongduk.ac.kr",
                    "1234", "1234");

            System.out.println("비밀번호 길이 검증 실패");

        } catch (IllegalArgumentException e) {
            System.out.println("비밀번호 길이 검증 성공: "
                    + e.getMessage());
        }

        try {
            service.signup("20201234@dongduk.ac.kr",
                    "test1234", "test5678");

            System.out.println("비밀번호 일치 검증 실패");

        } catch (IllegalArgumentException e) {
            System.out.println("비밀번호 일치 검증 성공: "
                    + e.getMessage());
        }

        User user = service.getProfile(20201234);

        System.out.println("회원 조회 성공: " + (user != null));

        if (user != null) {
            try {
                service.login(user.getEmail(), "wrongPassword");

                System.out.println("로그인 검증 실패");

            } catch (IllegalArgumentException e) {
                System.out.println("로그인 검증 성공: "
                        + e.getMessage());
            }
        }
    }
}