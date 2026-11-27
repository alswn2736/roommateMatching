package model.group;

import java.io.IOException;
import java.io.Reader;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
public final class GroupDatabase {

    private GroupDatabase() {
    }

    public static SqlSessionFactory create(DataSource dataSource) {
        try (Reader reader = Resources.getResourceAsReader("mybatis/group/config.xml")) {
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
            factory.getConfiguration().setEnvironment(new Environment("group", new JdbcTransactionFactory(), dataSource));
            return factory;
        }
        catch (IOException e) {
            throw new IllegalStateException("그룹 MyBatis 설정 파일 읽기 실패", e);
        }
    }

    public static SqlSessionFactory fromEnvironment() {
        String url = required("GROUP_DB_URL");
        String user = required("GROUP_DB_USER");
        String password = required("GROUP_DB_PASSWORD");
        return create(new PooledDataSource("oracle.jdbc.OracleDriver", url, user, password));
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(name + " 설정 필요");
        }
        return value;
    }
}
