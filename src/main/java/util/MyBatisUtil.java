package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * MyBatis SqlSessionFactory 를 한 번만 만들어 두고 재사용한다.
 * DB 접속 정보는 DBUtil 의 상수를 그대로 사용한다. (mybatis-config.xml 의 ${...} 에 주입)
 */
public class MyBatisUtil {

    private static final String CONFIG = "util/mybatis-config.xml";
    private static final SqlSessionFactory factory;

    static {
        Properties props = new Properties();
        props.setProperty("driver", DBUtil.DRIVER);
        props.setProperty("url", DBUtil.URL);
        props.setProperty("username", DBUtil.USER);
        props.setProperty("password", DBUtil.PASSWD);

        try (InputStream in = Resources.getResourceAsStream(CONFIG)) {
            factory = new SqlSessionFactoryBuilder().build(in, props);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    CONFIG + " 파일을 찾을 수 없습니다. (target/classes 에 xml 이 복사되었는지 확인하세요) " + e.getMessage());
        }
    }

    private MyBatisUtil() {}

    public static SqlSessionFactory getFactory() {
        return factory;
    }

    // autoCommit = false. 끝나면 commit()/rollback() 후 close() (try-with-resources 권장)
    public static SqlSession openSession() {
        return factory.openSession(false);
    }
}
