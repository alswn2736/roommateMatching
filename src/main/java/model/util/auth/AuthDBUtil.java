package model.util.auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// auth용 Oracle DB 연결 클래스
public class AuthDBUtil {

    private static final String URL =
            "jdbc:oracle:thin:@dblab.dongduk.ac.kr:1521/orclpdb";

    private static final String USERNAME = "dbp260105";

    private static final String PASSWORD =
            System.getenv("ROOMMATE_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC 드라이버 로딩 실패", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}