package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.RuleDTO;

public class RuleDAO {

    private RuleDTO makeDTO(ResultSet rs) throws SQLException {
        RuleDTO dto = new RuleDTO();
        dto.setRuleId(rs.getInt("ruleid"));
        if (rs.wasNull()) {
            dto.setRuleId(0);
        }
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setContent(rs.getString("content"));
        return dto;
    }

    public List<RuleDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<RuleDTO> list = new ArrayList<RuleDTO>();
        String sql = "SELECT ruleid, groupid, content FROM rule WHERE groupid = ? ORDER BY ruleid, groupid";

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

    public int insert(Connection conn, RuleDTO dto) throws SQLException {
        if (dto.getRuleId() == 0) {
            throw new IllegalArgumentException("ruleid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "INSERT INTO rule (ruleid, groupid, content) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getRuleId());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setString(3, dto.getContent());
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, RuleDTO dto) throws SQLException {
        if (dto.getRuleId() == 0) {
            throw new IllegalArgumentException("ruleid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "UPDATE rule SET content = ? WHERE ruleid = ? AND groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getContent());
            pstmt.setLong(2, dto.getRuleId());
            pstmt.setLong(3, dto.getGroupId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long ruleId, Long groupId) throws SQLException {
        if (ruleId == null) {
            throw new IllegalArgumentException("ruleid 값이 필요합니다.");
        }
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "DELETE FROM rule WHERE ruleid = ? AND groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ruleId);
            pstmt.setLong(2, groupId);
            return pstmt.executeUpdate();
        }
    }
}