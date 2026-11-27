package model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.dto.GroupDTO;
import model.dto.PostDTO;
import model.dto.mypage.ApplicantDTO;
import model.dto.mypage.ProfileView;

/** 마이페이지 전용 MyBatis 매퍼 (SQL 은 MyPageMapper.xml) */
public interface MyPageMapper {

    // ===== 프로필 =====
    int updateVisibility(@Param("studentId") int studentId,
                         @Param("field") String field,
                         @Param("value") String value);

    // ===== 계정 =====
    int updatePassword(@Param("studentId") int studentId, @Param("password") String password);

    // ===== 신청자 관리 =====
    List<PostDTO> selectPostsByWriter(int writerId);

    List<ApplicantDTO> selectApplicantsByPost(int postId);

    int increaseAcceptedCount(int postId);

    int updatePostStatus(@Param("postId") int postId, @Param("status") String status);

    int rejectPendingByPost(int postId);

    List<Integer> selectAcceptedApplicantIds(int postId);

    int nextGroupId();

    // ===== 매칭 정보 =====
    List<GroupDTO> selectGroupsByStudent(int studentId);

    List<ProfileView> selectMembersByGroup(int groupId);

    int countGroupMember(@Param("groupId") int groupId, @Param("studentId") int studentId);

    int countPendingLeave(@Param("groupId") int groupId, @Param("studentId") int studentId);

    int nextLeaveId();

    // ===== 회원 탈퇴 (순서대로 실행) =====
    int deleteChatsOfUser(int studentId);

    int deleteAppliesOfUser(int studentId);

    int deletePostsOfUser(int studentId);

    int deleteRuleAgreementsOfUser(int studentId);

    int deleteLeaveConsentsOfUser(int studentId);

    int clearLeaveRequester(int studentId);

    int clearRoleAssignee(int studentId);

    int clearSettlementPayer(int studentId);

    int deleteGroupMembershipsOfUser(int studentId);

    int deleteUser(int studentId);
}
