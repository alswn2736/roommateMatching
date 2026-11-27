package model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import model.dao.ApplyDAO;
import model.dao.ChatDAO;
import model.dao.GroupDAO;
import model.dao.GroupMemberDAO;
import model.dao.LeaveGroupDAO;
import model.dao.PostDAO;
import model.dao.UserDAO;
import model.dto.ApplyDTO;
import model.dto.ChatDTO;
import model.dto.GroupDTO;
import model.dto.GroupMemberDTO;
import model.dto.LeaveGroupDTO;
import model.dto.PostDTO;
import model.dto.UserDTO;
import model.dto.mypage.ApplicantDTO;
import model.dto.mypage.MyGroupDTO;
import model.dto.mypage.MyPostDTO;
import model.dto.mypage.ProfileView;
import model.mapper.MyPageMapper;
import util.MyBatisUtil;

/**
 * 마이페이지 비즈니스 로직.
 *  - 단순 조회/수정 : 기존 JDBC DAO (UserDAO, ApplyDAO, ChatDAO, PostDAO, GroupDAO ...)
 *  - 조인 조회, 동적 SQL : MyBatis (MyPageMapper.xml)
 * 하나의 SqlSession 이 가진 Connection 을 JDBC DAO 에도 넘겨서 같은 트랜잭션으로 처리한다.
 */
public class MyPageService {

    private static final MyPageService instance = new MyPageService();

    public static final String LEAVE_PENDING = "PENDING";
    public static final int CHAT_MAX_LENGTH = 1000;

    private final ApplyDAO applyDAO = new ApplyDAO();
    private final ChatDAO chatDAO = new ChatDAO();
    private final PostDAO postDAO = new PostDAO();
    private final GroupDAO groupDAO = new GroupDAO();
    private final GroupMemberDAO groupMemberDAO = new GroupMemberDAO();
    private final LeaveGroupDAO leaveGroupDAO = new LeaveGroupDAO();

    private MyPageService() {}

    public static MyPageService getInstance() {
        return instance;
    }

    // ============================================================
    // 공통
    // ============================================================

    /** 로그인 사용자 정보 (비밀번호 제외) */
    public ProfileView getMe(int studentId) throws SQLException, UserNotFoundException {
        UserDTO user = UserService.getInstance().findUser(studentId);
        return ProfileView.from(user);
    }

    // ============================================================
    // 프로필 관리
    // ============================================================

    public void updateVisibility(int studentId, String field, boolean open) throws MyPageException {
        if (!"namePublic".equals(field) && !"studentIdPublic".equals(field) && !"majorPublic".equals(field)) {
            throw new MyPageException("변경할 수 없는 항목입니다.");
        }
        try (SqlSession session = MyBatisUtil.openSession()) {
            int n = session.getMapper(MyPageMapper.class).updateVisibility(studentId, field, open ? "Y" : "N");
            if (n == 0) {
                throw new MyPageException("사용자 정보를 찾을 수 없습니다.");
            }
            session.commit();
        }
    }

    public void updateProfileImg(int studentId, String imgPath) throws SQLException, MyPageException {
        try (SqlSession session = MyBatisUtil.openSession()) {
            UserDAO userDAO = new UserDAO(session.getConnection());
            if (userDAO.updateProfileImg(studentId, imgPath) == 0) {
                throw new MyPageException("사용자 정보를 찾을 수 없습니다.");
            }
            session.commit(true);
        }
    }

    // ============================================================
    // 계정 설정
    // ============================================================

    public void changePassword(int studentId, String newPw, String confirmPw) throws MyPageException {
        if (newPw == null || newPw.isEmpty()) {
            throw new MyPageException("새 비밀번호를 입력해주세요.");
        }
        if (newPw.length() < 8) {
            throw new MyPageException("비밀번호는 8자리 이상이어야 합니다.");
        }
        if (!newPw.equals(confirmPw)) {
            throw new MyPageException("비밀번호 확인이 일치하지 않습니다.");
        }
        try (SqlSession session = MyBatisUtil.openSession()) {
            // TODO: auth 쪽에서 비밀번호를 암호화(해시)해서 저장한다면 여기서도 같은 방식으로 변환해야 함
            session.getMapper(MyPageMapper.class).updatePassword(studentId, newPw);
            session.commit();
        }
    }

    /** 회원 탈퇴: 관련 데이터를 FK 순서대로 정리한 뒤 users 삭제 (하나의 트랜잭션) */
    public void withdraw(int studentId) throws MyPageException {
        try (SqlSession session = MyBatisUtil.openSession()) {
            MyPageMapper m = session.getMapper(MyPageMapper.class);
            try {
                m.deleteChatsOfUser(studentId);
                m.deleteAppliesOfUser(studentId);
                m.deletePostsOfUser(studentId);
                m.deleteRuleAgreementsOfUser(studentId);
                m.deleteLeaveConsentsOfUser(studentId);
                m.clearLeaveRequester(studentId);
                m.clearRoleAssignee(studentId);
                m.clearSettlementPayer(studentId);
                m.deleteGroupMembershipsOfUser(studentId);
                if (m.deleteUser(studentId) == 0) {
                    throw new MyPageException("사용자 정보를 찾을 수 없습니다.");
                }
                session.commit();
            } catch (RuntimeException e) {
                session.rollback(true);
                e.printStackTrace();
                throw new MyPageException("탈퇴 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            }
        }
    }

    // ============================================================
    // 신청자 관리
    // ============================================================

    public List<MyPostDTO> getMyPosts(int writerId) {
        List<MyPostDTO> result = new ArrayList<MyPostDTO>();
        try (SqlSession session = MyBatisUtil.openSession()) {
            MyPageMapper m = session.getMapper(MyPageMapper.class);
            for (PostDTO post : m.selectPostsByWriter(writerId)) {
                result.add(new MyPostDTO(post, m.selectApplicantsByPost(post.getPostId())));
            }
        }
        return result;
    }

    /**
     * 신청 수락. 정원이 다 차면 모집글을 마감하고, 남은 대기 신청은 거절 처리하고,
     * 작성자 + 수락된 신청자로 룸메이트 그룹을 만든다.
     * @return 그룹이 생성되었으면 true
     */
    public boolean acceptApply(int me, int applyId) throws SQLException, MyPageException {
        try (SqlSession session = MyBatisUtil.openSession()) {
            Connection conn = session.getConnection();
            MyPageMapper m = session.getMapper(MyPageMapper.class);
            try {
                ApplyDTO apply = applyDAO.selectOne(conn, applyId);
                PostDTO post = apply == null ? null : postDAO.getPost(conn, apply.getPostId());
                if (apply == null || post == null || post.getWriterId() != me) {
                    throw new MyPageException("처리할 수 없는 신청입니다.");
                }
                if (!ApplyDTO.PENDING.equals(apply.getStatus())) {
                    throw new MyPageException("이미 처리된 신청입니다.");
                }
                if (!MyPostDTO.isRecruiting(post)) {
                    throw new MyPageException("이미 모집이 마감된 글입니다.");
                }

                applyDAO.updateStatus(conn, applyId, ApplyDTO.ACCEPTED);
                m.increaseAcceptedCount(post.getPostId());
                post.setAcceptedCount(post.getAcceptedCount() + 1);

                boolean groupCreated = false;
                if (MyPostDTO.isFull(post)) {
                    m.updatePostStatus(post.getPostId(), MyPostDTO.CLOSED);
                    m.rejectPendingByPost(post.getPostId());
                    createGroup(conn, m, post);
                    groupCreated = true;
                }
                session.commit(true);
                return groupCreated;
            } catch (MyPageException | SQLException | RuntimeException e) {
                session.rollback(true);
                throw e;
            }
        }
    }

    private void createGroup(Connection conn, MyPageMapper m, PostDTO post) throws SQLException {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int semester;
        if (month >= 3 && month <= 8) {
            semester = 1;
        } else {
            semester = 2;
            if (month <= 2) year -= 1; // 1~2월은 전년도 2학기(겨울)로 본다
        }

        int groupId = m.nextGroupId();
        groupDAO.insert(conn, new GroupDTO(groupId, post.getCapacity(), year, semester, GroupDTO.ACTIVE));

        groupMemberDAO.insert(conn, new GroupMemberDTO(groupId, post.getWriterId()));
        for (Integer applicantId : m.selectAcceptedApplicantIds(post.getPostId())) {
            if (applicantId != post.getWriterId()) {
                groupMemberDAO.insert(conn, new GroupMemberDTO(groupId, applicantId));
            }
        }
    }

    public void rejectApply(int me, int applyId) throws SQLException, MyPageException {
        try (SqlSession session = MyBatisUtil.openSession()) {
            Connection conn = session.getConnection();
            ApplyDTO apply = applyDAO.selectOne(conn, applyId);
            PostDTO post = apply == null ? null : postDAO.getPost(conn, apply.getPostId());
            if (apply == null || post == null || post.getWriterId() != me) {
                throw new MyPageException("처리할 수 없는 신청입니다.");
            }
            if (!ApplyDTO.PENDING.equals(apply.getStatus())) {
                throw new MyPageException("이미 처리된 신청입니다.");
            }
            applyDAO.updateStatus(conn, applyId, ApplyDTO.REJECTED);
            session.commit(true);
        }
    }

    // ============================================================
    // 문의 채팅 (신청 1건 = 채팅방 1개)
    // ============================================================

    /** 모집글 작성자 또는 신청자 본인만 채팅에 접근할 수 있다 */
    private ApplyDTO checkChatAccess(Connection conn, int me, int applyId) throws SQLException, MyPageException {
        ApplyDTO apply = applyDAO.selectOne(conn, applyId);
        if (apply == null) {
            throw new MyPageException("존재하지 않는 채팅입니다.");
        }
        if (apply.getApplicantId() != me) {
            PostDTO post = postDAO.getPost(conn, apply.getPostId());
            if (post == null || post.getWriterId() != me) {
                throw new MyPageException("채팅에 접근할 권한이 없습니다.");
            }
        }
        return apply;
    }

    public List<ChatDTO> getChats(int me, int applyId, int afterMsgId) throws SQLException, MyPageException {
        try (SqlSession session = MyBatisUtil.openSession()) {
            Connection conn = session.getConnection();
            checkChatAccess(conn, me, applyId);
            return chatDAO.selectAfter(conn, applyId, Math.max(afterMsgId, 0));
        }
    }

    public ChatDTO sendChat(int me, int applyId, String content) throws SQLException, MyPageException {
        if (content == null || content.trim().isEmpty()) {
            throw new MyPageException("메시지를 입력해주세요.");
        }
        content = content.trim();
        if (content.length() > CHAT_MAX_LENGTH) {
            throw new MyPageException("메시지는 " + CHAT_MAX_LENGTH + "자까지 보낼 수 있습니다.");
        }
        try (SqlSession session = MyBatisUtil.openSession()) {
            Connection conn = session.getConnection();
            ApplyDTO apply = checkChatAccess(conn, me, applyId);
            if (ApplyDTO.REJECTED.equals(apply.getStatus())) {
                throw new MyPageException("거절된 신청에는 메시지를 보낼 수 없습니다.");
            }
            ChatDTO chat = new ChatDTO();
            chat.setApplyId(applyId);
            chat.setWriterId(me);
            chat.setContent(content);
            chatDAO.insert(conn, chat);
            session.commit(true);
            return chat;
        }
    }

    // ============================================================
    // 매칭 정보
    // ============================================================

    public List<MyGroupDTO> getMyGroups(int me) {
        List<MyGroupDTO> result = new ArrayList<MyGroupDTO>();
        try (SqlSession session = MyBatisUtil.openSession()) {
            MyPageMapper m = session.getMapper(MyPageMapper.class);
            for (GroupDTO g : m.selectGroupsByStudent(me)) {
                MyGroupDTO dto = new MyGroupDTO();
                dto.setGroup(g);

                List<ProfileView> members = m.selectMembersByGroup(g.getGroupId());
                dto.setMembers(members);

                StringBuilder names = new StringBuilder();
                for (ProfileView p : members) {
                    if (p.getStudentId() == me) continue;
                    if (names.length() > 0) names.append(", ");
                    names.append(p.getDisplayName());
                }
                dto.setMateNames(names.length() == 0 ? "없음" : names.toString());
                dto.setLeavePending(m.countPendingLeave(g.getGroupId(), me) > 0);
                result.add(dto);
            }
        }
        return result;
    }

    public void requestLeave(int me, int groupId, String reason) throws SQLException, MyPageException {
        if (reason != null) {
            reason = reason.trim();
            if (reason.length() > 1000) {
                throw new MyPageException("탈퇴 사유는 1000자까지 입력할 수 있습니다.");
            }
            if (reason.isEmpty()) reason = null;
        }
        try (SqlSession session = MyBatisUtil.openSession()) {
            Connection conn = session.getConnection();
            MyPageMapper m = session.getMapper(MyPageMapper.class);

            if (m.countGroupMember(groupId, me) == 0) {
                throw new MyPageException("소속된 그룹이 아닙니다.");
            }
            List<GroupDTO> groups = groupDAO.findByGroup(conn, (long) groupId);
            if (groups.isEmpty() || !GroupDTO.ACTIVE.equalsIgnoreCase(groups.get(0).getStatus())) {
                throw new MyPageException("이미 종료된 그룹입니다.");
            }
            if (m.countPendingLeave(groupId, me) > 0) {
                throw new MyPageException("이미 탈퇴 요청을 보냈습니다. 룸메이트들의 동의를 기다려주세요.");
            }

            LeaveGroupDTO leave = new LeaveGroupDTO();
            leave.setLeaveId(m.nextLeaveId());
            leave.setGroupId(groupId);
            leave.setRequesterId(me);
            leave.setReason(reason);
            leave.setStatus(LEAVE_PENDING);
            leaveGroupDAO.insert(conn, leave);
            session.commit(true);
        }
    }
}
