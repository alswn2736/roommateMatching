package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.LeaveGroupDTO;

public class LeaveGroupDAO {

    private LeaveGroupDTO makeDTO(ResultSet rs) throws SQLException {
        LeaveGroupDTO dto = new LeaveGroupDTO();
        dto.setLeaveId(rs.getInt("leaveid"));
        if (rs.wasNull()) {
            dto.setLeaveId(0);
        }
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setRequesterId(rs.getInt("requesterid"));
        if (rs.wasNull()) {
            dto.setRequesterId(0);
        }
        dto.setReason(rs.getString("reason"));
        dto.setStatus(rs.getString("status"));
        return dto;
    }

    public List<LeaveGroupDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<LeaveGroupDTO> list = new ArrayList<LeaveGroupDTO>();
        String sql = "SELECT leaveid, groupid, requesterid, reason, status FROM leavegroup WHERE groupid = ? ORDER BY leaveid, groupid";

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

    public int insert(Connection conn, LeaveGroupDTO dto) throws SQLException {
        if (dto.getLeaveId() == 0) {
            throw new IllegalArgumentException("leaveid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "INSERT INTO leavegroup (leaveid, groupid, requesterid, reason, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getLeaveId());
            pstmt.setLong(2, dto.getGroupId());
            if (dto.getRequesterId() == 0) {
                pstmt.setNull(3, Types.NUMERIC);
            } else {
                pstmt.setLong(3, dto.getRequesterId());
            }
            pstmt.setString(4, dto.getReason());
            pstmt.setString(5, dto.getStatus());
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, LeaveGroupDTO dto) throws SQLException {
        if (dto.getLeaveId() == 0) {
            throw new IllegalArgumentException("leaveid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "UPDATE leavegroup SET requesterid = ?, reason = ?, status = ? WHERE leaveid = ? AND groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (dto.getRequesterId() == 0) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, dto.getRequesterId());
            }
            pstmt.setString(2, dto.getReason());
            pstmt.setString(3, dto.getStatus());
            pstmt.setLong(4, dto.getLeaveId());
            pstmt.setLong(5, dto.getGroupId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long leaveId, Long groupId) throws SQLException {
        if (leaveId == null) {
            throw new IllegalArgumentException("leaveid 값이 필요합니다.");
        }
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "DELETE FROM leavegroup WHERE leaveid = ? AND groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, leaveId);
            pstmt.setLong(2, groupId);
            return pstmt.executeUpdate();
        }
    }
}