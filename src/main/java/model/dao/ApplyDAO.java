package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.dto.ApplyDTO;

public class ApplyDAO {

    private static final String COLS = "applyid, applicantid, postid, introduction, status, createat";

    private ApplyDTO makeDTO(ResultSet rs) throws SQLException {
        ApplyDTO a = new ApplyDTO();
        a.setApplyId(rs.getInt("applyid"));
        a.setApplicantId(rs.getInt("applicantid"));
        a.setPostId(rs.getInt("postid"));
        a.setIntroduction(rs.getString("introduction"));
        a.setStatus(rs.getString("status"));
        a.setCreateAt(rs.getTimestamp("createat"));
        return a;
    }

    // 새 신청 번호 (현재 최대값 + 1)
    private int nextApplyId(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(applyid), 0) + 1 FROM apply";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public int insert(Connection conn, ApplyDTO a) throws SQLException {
        a.setApplyId(nextApplyId(conn));
        if (a.getCreateAt() == null) {
            a.setCreateAt(new Timestamp(System.currentTimeMillis()));
        }
        if (a.getStatus() == null) {
            a.setStatus(ApplyDTO.PENDING);
        }
        String sql = "INSERT INTO apply (" + COLS + ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, a.getApplyId());
            pstmt.setInt(2, a.getApplicantId());
            pstmt.setInt(3, a.getPostId());
            pstmt.setString(4, a.getIntroduction());
            pstmt.setString(5, a.getStatus());
            pstmt.setTimestamp(6, a.getCreateAt());
            return pstmt.executeUpdate();
        }
    }

    // 신청 1건 조회 (없으면 null)
    public ApplyDTO selectOne(Connection conn, int applyId) throws SQLException {
        String sql = "SELECT " + COLS + " FROM apply WHERE applyid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? makeDTO(rs) : null;
            }
        }
    }

    // 특정 글의 신청자 목록 (신청자 관리 화면)
    public List<ApplyDTO> selectByPost(Connection conn, int postId) throws SQLException {
        List<ApplyDTO> list = new ArrayList<ApplyDTO>();
        String sql = "SELECT " + COLS + " FROM apply WHERE postid = ? ORDER BY createat";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    //내가 한 신청 목록
    public List<ApplyDTO> selectByApplicant(Connection conn, int applicantId) throws SQLException {
        List<ApplyDTO> list = new ArrayList<ApplyDTO>();
        String sql = "SELECT " + COLS + " FROM apply WHERE applicantid = ? ORDER BY createat DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    // 중복 신청 확인
    public boolean existsByPostAndApplicant(Connection conn, int postId, int applicantId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM apply WHERE postid = ? AND applicantid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, applicantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public int updateStatus(Connection conn, int applyId, String status) throws SQLException {
        String sql = "UPDATE apply SET status = ? WHERE applyid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, applyId);
            return pstmt.executeUpdate();
        }
    }

    public int updateIntroduction(Connection conn, int applyId, String introduction) throws SQLException {
        String sql = "UPDATE apply SET introduction = ? WHERE applyid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, introduction);
            pstmt.setInt(2, applyId);
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, int applyId) throws SQLException {
        String sql = "DELETE FROM apply WHERE applyid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applyId);
            return pstmt.executeUpdate();
        }
    }
}