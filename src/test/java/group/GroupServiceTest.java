package group;

import model.group.*;
import model.dto.*;
import java.sql.Connection;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class GroupServiceTest {
    GroupTestDatabase db;
    @BeforeEach void setup() throws Exception { db=new GroupTestDatabase(); }

    @Test void privateGroupAndTermMembership() throws Exception {
        assertEquals(1,db.service.getInGroups(1).size());
        assertEquals(0,db.service.getInGroups(99).size());
        assertEquals(3,db.service.getDetail(100,1).getMembers().size());
        try {
            db.service.getDetail(100,4);
            fail("예외가 발생해야 함.");
        } catch (GroupException e) {
            assertEquals(403, e.getStatus());
        }
        assertTrue(db.service.hasActiveGroup(1,2026,2));
        assertFalse(db.service.hasActiveGroup(1,2026,1));
    }
    @Test void roleCrudAndCrossGroupProtection() throws Exception {
        db.service.saveRole(100,1,0,"청소",2);
        RoleDTO role=db.service.getDetail(100,1).getRoles().get(0);
        db.service.saveRole(100,2,role.getRoleId(),"설거지",3);
        assertEquals("설거지",db.service.getDetail(100,1).getRoles().get(0).getRoleTitle());
        try {
            db.service.saveRole(100,1,0,"외부 배정",4);
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        try {
            db.service.saveRole(200,4,role.getRoleId(),"다른 그룹",4);
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        db.service.deleteRole(100,3,role.getRoleId());
        assertEquals(0,db.count("SELECT COUNT(*) FROM role"));
    }
    @Test void ruleAgreementIsIdempotentAndResetOnEdit() throws Exception {
        db.service.saveRule(100,1,0,"조용히 생활하기");
        int rule=db.service.getDetail(100,1).getRules().get(0).getRuleId();
        db.service.agreeRule(100,1,rule);db.service.agreeRule(100,1,rule);db.service.agreeRule(100,2,rule);
        assertEquals(2,db.service.getDetail(100,1).getAgreementCounts().get(rule));
        db.service.saveRule(100,3,rule,"밤 12시 이후 조용히");
        assertEquals(0,db.count("SELECT COUNT(*) FROM ruleagreement"));
        db.service.agreeRule(100,2,rule);db.service.deleteRule(100,1,rule);
        assertEquals(0,db.count("SELECT COUNT(*) FROM ruleagreement"));
    }
    @Test void leaveRequiresAllOtherMembersAndReassignsRoles() throws Exception {
        db.service.saveRole(100,1,0,"청소",1);
        int request=db.service.requestLeave(100,1,"이사");
        assertEquals(2,db.count("SELECT COUNT(*) FROM leaveconsent"));
        try {
            db.service.answerLeave(100,1,request,"Y");
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        try {
            db.service.answerLeave(100,4,request,"Y");
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        db.service.answerLeave(100,2,request,"Y");
        assertEquals(3,db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100"));
        db.service.answerLeave(100,3,request,"Y");
        assertEquals(0,db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100 AND studentid=1"));
        assertEquals(3,db.count("SELECT assigneeid FROM role"));
        assertEquals(1,db.count("SELECT COUNT(*) FROM leavegroup WHERE status='APPROVED'"));
        try {
            db.service.answerLeave(100,2,request,"Y");
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
    }
    @Test void rejectingLeaveKeepsMemberAndAllowsNewRequest() throws Exception {
        int request=db.service.requestLeave(100,1,"");
        try {
            db.service.requestLeave(100,2,"동시");
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        db.service.answerLeave(100,2,request,"N");
        assertEquals(3,db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100"));
        assertEquals(1,db.count("SELECT COUNT(*) FROM leavegroup WHERE status='REJECTED'"));
        db.service.requestLeave(100,1,"다시");
    }
    @Test void leaveApprovalFailureRollsBackVoteAndMembership() throws Exception {
        int id=db.service.requestLeave(100,1,"퇴실");db.service.answerLeave(100,2,id,"Y");
        db.execute("ALTER TABLE leavegroup ADD CONSTRAINT no_approval CHECK(status<>'APPROVED')");
        try {
            db.service.answerLeave(100,3,id,"Y");
            fail("예외가 발생해야 함.");
        } catch (Exception expected) {
            assertNotNull(expected);
        }
        assertEquals(1,db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100 AND studentid=1"));
        assertEquals(1,db.count("SELECT COUNT(*) FROM leaveconsent WHERE memberid=3 AND agreed IS NULL"));
    }
    @Test void simultaneousLeaveRequestsProduceOnePendingRequest() throws Exception {
        ExecutorService pool=Executors.newFixedThreadPool(2);CountDownLatch start=new CountDownLatch(1);
        try {
            Callable<Boolean> a = new LeaveRequestTask(db, start, 1);
            Callable<Boolean> b = new LeaveRequestTask(db, start, 2);
            Future<Boolean> f=pool.submit(a),g=pool.submit(b);start.countDown();
            assertNotEquals(f.get(10,TimeUnit.SECONDS),g.get(10,TimeUnit.SECONDS));
            assertEquals(1,db.count("SELECT COUNT(*) FROM leavegroup WHERE status='PENDING'"));
        } finally {pool.shutdownNow();}
    }
    @Test void completedGroupCannotBeEdited() throws Exception {
        db.execute("UPDATE groups SET status='ENDED' WHERE groupid=100");
        try {
            db.service.saveRole(100,1,0,"청소",1);
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        assertEquals("ENDED",db.service.getDetail(100,1).getGroup().getStatus());
    }
    @Test void groupCreationParticipatesInCallerTransaction() throws Exception {
        try(Connection c=db.dataSource.getConnection()) {
            c.setAutoCommit(false);
            assertEquals(300,db.service.createGroup(c,300,2026,2).getGroupId());
            assertEquals(300,db.service.createGroup(c,300,2026,2).getGroupId());
            c.rollback();
        }
        assertEquals(0,db.count("SELECT COUNT(*) FROM groups WHERE groupid=300"));
        try(Connection c=db.dataSource.getConnection()) {c.setAutoCommit(false);db.service.createGroup(c,300,2026,2);c.commit();}
        assertEquals(2,db.count("SELECT COUNT(*) FROM group_member WHERE groupid=300"));
    }
    @Test void groupDetailDoesNotNeedSettlementTables() throws Exception {
        db.execute("DROP TABLE settlementlist");
        db.execute("DROP TABLE settlement");
        assertEquals(3,db.service.getDetail(100,1).getMembers().size());
    }
    @Test void savingUnchangedRuleKeepsAgreements() throws Exception {
        db.service.saveRule(100, 1, 0, "조용히 생활하기");
        int ruleId = db.count("SELECT ruleid FROM rule");
        db.service.agreeRule(100, 2, ruleId);
        db.service.saveRule(100, 1, ruleId, "  조용히 생활하기  ");
        assertEquals(1, db.count("SELECT COUNT(*) FROM ruleagreement"));
        try {
            db.service.saveRule(200, 4, ruleId, "조용히 생활하기");
            fail("예외가 발생해야 함.");
        } catch (GroupException e) {
            assertEquals(404, e.getStatus());
        }
        assertEquals(1, db.count("SELECT COUNT(*) FROM ruleagreement"));
    }

    @Test void changedMembershipRequiresNewLeaveRequest() throws Exception {
        int leaveId = db.service.requestLeave(100, 1, "이사");
        db.service.answerLeave(100, 2, leaveId, "Y");
        db.execute("INSERT INTO group_member VALUES(100, 4)");
        try {
            db.service.answerLeave(100, 3, leaveId, "Y");
            fail("예외가 발생해야 합니다.");
        } catch (GroupException e) {
            assertEquals(409, e.getStatus());
        }
        assertEquals(4, db.count("SELECT COUNT(*) FROM group_member WHERE groupid=100"));
        assertEquals(1, db.count("SELECT COUNT(*) FROM leaveconsent WHERE memberid=3 AND agreed IS NULL"));
        db.service.answerLeave(100, 3, leaveId, "N");
        int newLeaveId = db.service.requestLeave(100, 1, "다시 요청");
        assertEquals(3, db.count("SELECT COUNT(*) FROM leaveconsent WHERE leaveid=" + newLeaveId));
    }

    @Test void detailSeparatesLeaveHistoryAndCurrentResponsesByGroup() throws Exception {
        assertTrue(db.service.getDetail(100, 3).getConsents().isEmpty());
        int oldRequest = db.service.requestLeave(100, 1, "첫 요청");
        db.service.answerLeave(100, 2, oldRequest, "N");
        int currentRequest = db.service.requestLeave(100, 1, "다시 요청");
        db.service.answerLeave(100, 2, currentRequest, "Y");
        db.execute("INSERT INTO leavegroup VALUES(900,100,1,'과거 요청','REJECTED')");
        db.execute("INSERT INTO group_member VALUES(200,1)");
        int otherGroupRequest = db.service.requestLeave(200, 4, "다른 그룹");

        GroupPage page = db.service.getDetail(100, 3);

        assertEquals(3, page.getLeaveRequests().size());
        assertEquals(3, page.getConsents().size());
        assertEquals(2, page.getConsents().get(oldRequest).size());
        assertEquals("N", page.getConsents().get(oldRequest).get(0).getAgreed());
        assertNull(page.getConsents().get(oldRequest).get(1).getAgreed());

        assertEquals(2, page.getConsents().get(currentRequest).size());
        assertEquals("Y", page.getConsents().get(currentRequest).get(0).getAgreed());
        assertEquals(3, page.getConsents().get(currentRequest).get(1).getMemberId());
        assertNull(page.getConsents().get(currentRequest).get(1).getAgreed());

        assertTrue(page.getConsents().get(900).isEmpty());
        assertFalse(page.getConsents().containsKey(otherGroupRequest));

        assertEquals(1, page.getAnswerableLeaves().size());
        assertTrue(page.getAnswerableLeaves().contains(currentRequest));
        assertTrue(db.service.getDetail(100, 2).getAnswerableLeaves().isEmpty());
        assertTrue(db.service.getDetail(100, 1).getAnswerableLeaves().isEmpty());
    }

    @Test void invalidInputIsRejectedBeforeWrites() throws Exception {
        try {
            GroupPolicy.yesNo("YES");
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
        StringBuilder longReason = new StringBuilder();
        for (int i = 0; i < 301; i++) {
            longReason.append("가");
        }
        try {
            db.service.requestLeave(100,1,longReason.toString());
            fail("예외가 발생해야 함.");
        } catch (GroupException expected) {
            assertNotNull(expected);
        }
    }
    private static class LeaveRequestTask implements Callable<Boolean> {
        private final GroupTestDatabase db;
        private final CountDownLatch start;
        private final int memberId;

        LeaveRequestTask(GroupTestDatabase db, CountDownLatch start, int memberId) {
            this.db = db;
            this.start = start;
            this.memberId = memberId;
        }

        @Override
        public Boolean call() throws Exception {
            start.await();
            try {
                db.service.requestLeave(100, memberId, "");
                return true;
            } catch (GroupException e) {
                return false;
            }
        }
    }
}
