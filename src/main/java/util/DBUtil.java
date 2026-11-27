package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // MyBatisUtil 에서도 같은 접속 정보를 쓰도록 상수로 분리
    static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String URL = "jdbc:oracle:thin:@dblab.dongduk.ac.kr:1521/orclpdb";
    static final String USER = "dbp260105";
    static final String PASSWD = "122913";

    static {
        // JDBC 드라이버 로딩
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWD);
    }
}
