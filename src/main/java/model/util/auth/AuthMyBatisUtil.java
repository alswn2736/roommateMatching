package model.util.auth;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

// MyBatis 설정 및 SqlSession 생성 클래스
public class AuthMyBatisUtil {

    private static final SqlSessionFactory factory = createFactory();

    private static SqlSessionFactory createFactory() {

        try (InputStream input = Resources.getResourceAsStream(
                "mybatis/config/auth-mybatis-config.xml")) {

            return new SqlSessionFactoryBuilder().build(input);

        } catch (IOException e) {
            throw new IllegalStateException("MyBatis 설정 로딩 실패", e);
        }
    }

    public static SqlSession getSession(Connection conn) {
        return factory.openSession(conn);
    }
}