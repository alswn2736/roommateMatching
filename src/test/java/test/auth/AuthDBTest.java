package test.auth;

import java.sql.Connection;
import model.dao.UserDAO;
import model.util.auth.AuthDBUtil;

// DB 연결 및 UserDAO 조회 테스트
public class AuthDBTest {

    public static void main(String[] args) {

        try (Connection conn = AuthDBUtil.getConnection()) {

            System.out.println("DB 연결 성공");

            UserDAO dao = new UserDAO(conn);

            System.out.println("이메일 존재 여부: "
                    + dao.existsByEmail("20249999@dongduk.ac.kr"));

            System.out.println("DAO 조회 성공");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}