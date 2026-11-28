package model.group;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public final class GroupTransaction implements AutoCloseable {

    private final SqlSession session;

    private boolean committed;

    public GroupTransaction(SqlSessionFactory factory) {
        session = factory.openSession(false);
    }

    public SqlSession getSession() {
        return session;
    }

    public void commit() {
        session.commit(true);
        committed = true;
    }

    @Override
    public void close() {
        try {
            if (!committed) {
                session.rollback(true);
            }
        }
        finally {
            session.close();
        }
    }
}
