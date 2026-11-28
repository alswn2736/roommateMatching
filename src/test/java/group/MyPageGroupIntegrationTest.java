package group;

import java.io.Reader;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import model.service.MyPageException;
import model.service.MyPageService;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyPageGroupIntegrationTest {
    private GroupTestDatabase db;
    private MyPageService mypage;
    private SqlSessionFactory factory;

    @BeforeEach void setup() throws Exception {
        db = new GroupTestDatabase();
        Properties props = new Properties();
        props.setProperty("driver", "org.h2.Driver");
        props.setProperty("url", db.dataSource.getURL());
        props.setProperty("username", "");
        props.setProperty("password", "");
        try (Reader reader = Resources.getResourceAsReader("util/mybatis-config.xml")) {
            factory = new SqlSessionFactoryBuilder().build(reader, props);
        }
        mypage = new MyPageService(factory);
        for (String column : new String[]{"kwsmoking", "kwsleeptime", "kwsensitivity", "kwaircon", "kwclean", "status", "content"}) {
            db.execute("ALTER TABLE post ADD " + column + " VARCHAR(100)");
        }
        db.execute("ALTER TABLE post ADD createat TIMESTAMP");
        db.execute("UPDATE post SET acceptedcount=0,status='OPEN' WHERE postid=300");
        db.execute("UPDATE apply SET status='PENDING' WHERE postid=300");
    }

    @AfterEach void closePool() {
        if (factory != null) {
            ((PooledDataSource) factory.getConfiguration().getEnvironment().getDataSource()).forceCloseAll();
        }
    }

    @Test void acceptedApplicationCreatesGroupUsingPostId() throws Exception {
        assertTrue(mypage.acceptApply(1, 1));
        assertEquals(1, db.count("SELECT COUNT(*) FROM groups WHERE groupid=300"));
        assertEquals(2, db.service.getDetail(300, 2).getMembers().size());
        assertEquals("새 모집글", db.service.getDetail(300, 2).getTitle());
        assertEquals(1, db.count("SELECT COUNT(*) FROM post WHERE postid=300 AND status='CLOSED' AND acceptedcount=1"));
        try {
            mypage.acceptApply(1, 1);
            fail("처리한 신청은 다시 수락할 수 없어야 함");
        } catch (MyPageException expected) {
            assertNotNull(expected.getMessage());
        }
    }

    @Test void failedGroupCreationRollsBackAcceptance() throws Exception {
        db.execute("ALTER TABLE groups ADD CONSTRAINT reject_new_group CHECK(groupid<>300)");
        try {
            mypage.acceptApply(1, 1);
            fail("그룹 생성 실패가 전달되어야 함");
        } catch (SQLException expected) {
            assertNotNull(expected);
        }
        assertEquals(1, db.count("SELECT COUNT(*) FROM apply WHERE applyid=1 AND status='PENDING'"));
        assertEquals(1, db.count("SELECT COUNT(*) FROM post WHERE postid=300 AND status='OPEN' AND acceptedcount=0"));
        assertEquals(0, db.count("SELECT COUNT(*) FROM group_member WHERE groupid=300"));
    }

    @Test void mypageLeaveCreatesVotesAndCompletesInGroupPage() throws Exception {
        mypage.requestLeave(1, 100, "이사");
        int leaveId = db.count("SELECT leaveid FROM leavegroup WHERE groupid=100");
        assertEquals(2, db.count("SELECT COUNT(*) FROM leaveconsent WHERE groupid=100 AND agreed IS NULL"));
        assertTrue(db.service.getDetail(100, 2).getAnswerableLeaves().contains(leaveId));
        try {
            mypage.requestLeave(2, 100, "중복 요청");
            fail("같은 그룹의 진행 중 요청이 중복되면 안 됨");
        } catch (MyPageException expected) {
            assertNotNull(expected.getMessage());
        }
        db.service.answerLeave(100, 2, leaveId, "Y");
        db.service.answerLeave(100, 3, leaveId, "Y");
        assertEquals(0, db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100 AND studentid=1"));
        assertEquals(1, db.count("SELECT COUNT(*) FROM leavegroup WHERE status='APPROVED'"));
    }

    @Test void invalidMypageLeaveCannotCreatePartialRequest() throws Exception {
        StringBuilder reason = new StringBuilder();
        for (int i = 0; i < 301; i++) {
            reason.append("가");
        }
        try {
            mypage.requestLeave(1, 100, reason.toString());
            fail("사유 길이를 검사해야 함");
        } catch (MyPageException expected) {
            assertNotNull(expected.getMessage());
        }
        try {
            mypage.requestLeave(4, 100, "다른 그룹");
            fail("외부 구성원 요청을 거절해야 함");
        } catch (MyPageException expected) {
            assertNotNull(expected.getMessage());
        }
        assertEquals(0, db.count("SELECT COUNT(*) FROM leavegroup"));
        assertEquals(0, db.count("SELECT COUNT(*) FROM leaveconsent"));
    }

    @Test void accountWithdrawalCannotBypassActiveGroupLeave() throws Exception {
        try {
            mypage.withdraw(1);
            fail("그룹 퇴실 전에 계정을 삭제하면 안 됨");
        } catch (MyPageException expected) {
            assertTrue(expected.getMessage().contains("퇴실"));
        }
        assertEquals(1, db.count("SELECT COUNT(*) FROM users WHERE studentid=1"));
        assertEquals(3, db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100"));
    }

    @Test void concurrentAcceptsFillOnlyOneRemainingPlace() throws Exception {
        db.execute("INSERT INTO apply VALUES(2,3,300,'소개','PENDING',CURRENT_TIMESTAMP)");
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        try {
            Future<Boolean> first = pool.submit(new AcceptTask(mypage, start, 1));
            Future<Boolean> second = pool.submit(new AcceptTask(mypage, start, 2));
            start.countDown();
            assertNotEquals(first.get(10, TimeUnit.SECONDS), second.get(10, TimeUnit.SECONDS));
            assertEquals(1, db.count("SELECT acceptedcount FROM post WHERE postid=300"));
            assertEquals(1, db.count("SELECT COUNT(*) FROM apply WHERE postid=300 AND status='ACCEPTED'"));
            assertEquals(2, db.count("SELECT COUNT(*) FROM group_member WHERE groupid=300"));
        } finally {
            pool.shutdownNow();
        }
    }

    private static class AcceptTask implements Callable<Boolean> {
        private final MyPageService service;
        private final CountDownLatch start;
        private final int applyId;

        AcceptTask(MyPageService service, CountDownLatch start, int applyId) {
            this.service = service;
            this.start = start;
            this.applyId = applyId;
        }

        @Override public Boolean call() throws Exception {
            start.await();
            try {
                return service.acceptApply(1, applyId);
            } catch (MyPageException expected) {
                return false;
            }
        }
    }
}
