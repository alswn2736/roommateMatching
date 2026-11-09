package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.GroupMemberDTO;

public class GroupMemberDAO {

    private GroupMemberDTO makeDTO(ResultSet rs) throws SQLException {
        GroupMemberDTO dto = new GroupMemberDTO();
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setStudentId(rs.getInt("studentid"));
        if (rs.wasNull()) {
            dto.setStudentId(0);
        }
        return dto;
    }

    public List<GroupMemberDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<GroupMemberDTO> list = new ArrayList<GroupMemberDTO>();
        String sql = "SELECT groupid, studentid FROM group_member WHERE groupid = ? ORDER BY groupid, studentid";

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

    public List<GroupMemberDTO> findByStudent(Connection conn, Long studentId) throws SQLException {
        List<GroupMemberDTO> list = new ArrayList<GroupMemberDTO>();
        String sql = "SELECT groupid, studentid FROM group_member WHERE studentid = ? ORDER BY groupid";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (studentId == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, studentId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    public int insert(Connection conn, GroupMemberDTO dto) throws SQLException {
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getStudentId() == 0) {
            throw new IllegalArgumentException("studentid 값이 필요합니다.");
        }
        String sql = "INSERT INTO group_member (groupid, studentid) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getGroupId());
            pstmt.setLong(2, dto.getStudentId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long groupId, Long studentId) throws SQLException {
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (studentId == null) {
            throw new IllegalArgumentException("studentid 값이 필요합니다.");
        }
        String sql = "DELETE FROM group_member WHERE groupid = ? AND studentid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            pstmt.setLong(2, studentId);
            return pstmt.executeUpdate();
        }
    }
}