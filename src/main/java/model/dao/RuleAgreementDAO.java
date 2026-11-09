package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.RuleAgreementDTO;

public class RuleAgreementDAO {

    private RuleAgreementDTO makeDTO(ResultSet rs) throws SQLException {
        RuleAgreementDTO dto = new RuleAgreementDTO();
        dto.setRuleId(rs.getInt("ruleid"));
        if (rs.wasNull()) {
            dto.setRuleId(0);
        }
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setMemberId(rs.getInt("memberid"));
        if (rs.wasNull()) {
            dto.setMemberId(0);
        }
        return dto;
    }

    public List<RuleAgreementDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<RuleAgreementDTO> list = new ArrayList<RuleAgreementDTO>();
        String sql = "SELECT ruleid, groupid, memberid FROM ruleagreement WHERE groupid = ? ORDER BY ruleid, groupid, memberid";

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

    public List<RuleAgreementDTO> findByRule(Connection conn, Long groupId, Long ruleId) throws SQLException {
        List<RuleAgreementDTO> list = new ArrayList<RuleAgreementDTO>();
        String sql = "SELECT ruleid, groupid, memberid FROM ruleagreement WHERE groupid = ? AND ruleid = ? ORDER BY memberid";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (groupId == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, groupId);
            }
            if (ruleId == null) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setLong(2, ruleId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    public int insert(Connection conn, RuleAgreementDTO dto) throws SQLException {
        if (dto.getRuleId() == 0) {
            throw new IllegalArgumentException("ruleid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMemberId() == 0) {
            throw new IllegalArgumentException("memberid 값이 필요합니다.");
        }
        String sql = "INSERT INTO ruleagreement (ruleid, groupid, memberid) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getRuleId());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setLong(3, dto.getMemberId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long ruleId, Long groupId, Long memberId) throws SQLException {
        if (ruleId == null) {
            throw new IllegalArgumentException("ruleid 값이 필요합니다.");
        }
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (memberId == null) {
            throw new IllegalArgumentException("memberid 값이 필요합니다.");
        }
        String sql = "DELETE FROM ruleagreement WHERE ruleid = ? AND groupid = ? AND memberid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ruleId);
            pstmt.setLong(2, groupId);
            pstmt.setLong(3, memberId);
            return pstmt.executeUpdate();
        }
    }
}