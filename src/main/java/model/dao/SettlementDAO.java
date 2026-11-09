package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.SettlementDTO;

public class SettlementDAO {

    private SettlementDTO makeDTO(ResultSet rs) throws SQLException {
        SettlementDTO dto = new SettlementDTO();
        dto.setMonth(rs.getString("month"));
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setSettled(rs.getString("settled"));
        return dto;
    }

    public List<SettlementDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<SettlementDTO> list = new ArrayList<SettlementDTO>();
        String sql = "SELECT month, groupid, settled FROM settlement WHERE groupid = ? ORDER BY groupid, month";

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

    public List<SettlementDTO> findByGroupAndMonth(Connection conn, Long groupId, String month) throws SQLException {
        List<SettlementDTO> list = new ArrayList<SettlementDTO>();
        String sql = "SELECT month, groupid, settled FROM settlement WHERE groupid = ? AND month = ? ORDER BY groupid, month";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (groupId == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, groupId);
            }
            pstmt.setString(2, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    public int insert(Connection conn, SettlementDTO dto) throws SQLException {
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMonth() == null) {
            throw new IllegalArgumentException("month 값이 필요합니다.");
        }
        String sql = "INSERT INTO settlement (month, groupid, settled) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getMonth());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setString(3, dto.getSettled());
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, SettlementDTO dto) throws SQLException {
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMonth() == null) {
            throw new IllegalArgumentException("month 값이 필요합니다.");
        }
        String sql = "UPDATE settlement SET settled = ? WHERE groupid = ? AND month = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getSettled());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setString(3, dto.getMonth());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long groupId, String month) throws SQLException {
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (month == null) {
            throw new IllegalArgumentException("month 값이 필요합니다.");
        }
        String sql = "DELETE FROM settlement WHERE groupid = ? AND month = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            pstmt.setString(2, month);
            return pstmt.executeUpdate(); //영향을 받은 행 수를 반환, autoCommit을 꺼 뒀다면 나중에 conn.commit()을 해야 변경이 확정...기억
        }
    }
}