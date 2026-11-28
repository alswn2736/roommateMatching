package group;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.UUID;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import model.group.GroupDatabase;
import model.service.group.GroupService;

public class GroupTestDatabase {
    public final JdbcDataSource dataSource = new JdbcDataSource();
    public final GroupService service;
    public GroupTestDatabase() throws Exception {
        dataSource.setURL("jdbc:h2:mem:"+UUID.randomUUID()+";MODE=Oracle;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=5000;NON_KEYWORDS=GROUPS,ROLE,MONTH");
        try(Connection c=dataSource.getConnection()) {
            RunScript.execute(c,new InputStreamReader(getClass().getResourceAsStream("/group/schema.sql"),StandardCharsets.UTF_8));
        }
        service=new GroupService(GroupDatabase.create(dataSource));
    }
    public int count(String sql) throws SQLException {
        try(Connection c=dataSource.getConnection();Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){r.next();return r.getInt(1);}
    }
    public void execute(String sql) throws SQLException {
        try(Connection c=dataSource.getConnection();Statement s=c.createStatement()){s.execute(sql);}
    }
}
