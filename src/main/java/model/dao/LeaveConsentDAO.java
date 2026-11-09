package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.LeaveConsentDTO;

public class LeaveConsentDAO {

    private LeaveConsentDTO makeDTO(ResultSet rs) throws SQLException {
        LeaveConsentDTO dto = new LeaveConsentDTO();
        dto.setLeaveId(rs.getInt("leaveid"));
        if (rs.wasNull()) {
            dto.setLeaveId(0);
        }
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setMemberId(rs.getInt("memberid"));
        if (rs.wasNull()) {
            dto.setMemberId(0);
        }
        dto.setAgreed(rs.getString("agreed"));
        return dto;
    }

    public List<LeaveConsentDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<LeaveConsentDTO> list = new ArrayList<LeaveConsentDTO>();
        String sql = "SELECT leaveid, groupid, memberid, agreed FROM leaveconsent WHERE groupid = ? ORDER BY leaveid, groupid, memberid";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (groupId == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, groupId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    public List<LeaveConsentDTO> findByRequest(Connection conn, Long groupId, Long leaveId) throws SQLException {
        List<LeaveConsentDTO> list = new ArrayList<LeaveConsentDTO>();
        String sql = "SELECT leaveid, groupid, memberid, agreed FROM leaveconsent WHERE groupid = ? AND leaveid = ? ORDER BY memberid";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (groupId == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, groupId);
            }
            if (leaveId == null) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setLong(2, leaveId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    public int insert(Connection conn, LeaveConsentDTO dto) throws SQLException {
        if (dto.getLeaveId() == 0) {
            throw new IllegalArgumentException("leaveid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMemberId() == 0) {
            throw new IllegalArgumentException("memberid 값이 필요합니다.");
        }
        String sql = "INSERT INTO leaveconsent (leaveid, groupid, memberid, agreed) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getLeaveId());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setLong(3, dto.getMemberId());
            pstmt.setString(4, dto.getAgreed());
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, LeaveConsentDTO dto) throws SQLException {
        if (dto.getLeaveId() == 0) {
            throw new IllegalArgumentException("leaveid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMemberId() == 0) {
            throw new IllegalArgumentException("memberid 값이 필요합니다.");
        }
        String sql = "UPDATE leaveconsent SET agreed = ? WHERE leaveid = ? AND groupid = ? AND memberid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getAgreed());
            pstmt.setLong(2, dto.getLeaveId());
            pstmt.setLong(3, dto.getGroupId());
            pstmt.setLong(4, dto.getMemberId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long leaveId, Long groupId, Long memberId) throws SQLException {
        if (leaveId == null) {
            throw new IllegalArgumentException("leaveid 값이 필요합니다.");
        }
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (memberId == null) {
            throw new IllegalArgumentException("memberid 값이 필요합니다.");
        }
        String sql = "DELETE FROM leaveconsent WHERE leaveid = ? AND groupid = ? AND memberid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, leaveId);
            pstmt.setLong(2, groupId);
            pstmt.setLong(3, memberId);
            return pstmt.executeUpdate();
        }
    }
}