package test.auth;

import model.dao.UserMyBatisDAO;
import model.dto.UserDTO;

public class UserMyBatisTest {

    public static void main(String[] args) {

        try {
            UserMyBatisDAO dao = new UserMyBatisDAO();

            String email = "20201234@dongduk.ac.kr";

            UserDTO user = dao.selectByEmail(email);

            if (user == null) {
                System.out.println("회원이 존재하지 않습니다.");
                return;
            }

            System.out.println("이메일 조회 성공");
            System.out.println("학번: " + user.getStudentId());
            System.out.println("이메일: " + user.getEmail());
            System.out.println("닉네임: " + user.getNickname());

            UserDTO result = dao.selectById(user.getStudentId());

            System.out.println("학번 조회 성공 여부: "
                    + (result != null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}