package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.RoleDTO;

public class RoleDAO {

    private RoleDTO makeDTO(ResultSet rs) throws SQLException {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(rs.getInt("roleid"));
        if (rs.wasNull()) {
            dto.setRoleId(0);
        }
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setRoleTitle(rs.getString("roletitle"));
        dto.setAssigneeId(rs.getInt("assigneeid"));
        if (rs.wasNull()) {
            dto.setAssigneeId(0);
        }
        return dto;
    }

    public List<RoleDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<RoleDTO> list = new ArrayList<RoleDTO>();
        String sql = "SELECT roleid, groupid, roletitle, assigneeid FROM role WHERE groupid = ? ORDER BY roleid, groupid";

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

    public int insert(Connection conn, RoleDTO dto) throws SQLException {
        if (dto.getRoleId() == 0) {
            throw new IllegalArgumentException("roleid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "INSERT INTO role (roleid, groupid, roletitle, assigneeid) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getRoleId());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setString(3, dto.getRoleTitle());
            if (dto.getAssigneeId() == 0) {
                pstmt.setNull(4, Types.NUMERIC);
            } else {
                pstmt.setLong(4, dto.getAssigneeId());
            }
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, RoleDTO dto) throws SQLException {
        if (dto.getRoleId() == 0) {
            throw new IllegalArgumentException("roleid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "UPDATE role SET roletitle = ?, assigneeid = ? WHERE roleid = ? AND groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getRoleTitle());
            if (dto.getAssigneeId() == 0) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setLong(2, dto.getAssigneeId());
            }
            pstmt.setLong(3, dto.getRoleId());
            pstmt.setLong(4, dto.getGroupId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long roleId, Long groupId) throws SQLException {
        if (roleId == null) {
            throw new IllegalArgumentException("roleid 값이 필요합니다.");
        }
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "DELETE FROM role WHERE roleid = ? AND groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, roleId);
            pstmt.setLong(2, groupId);
            return pstmt.executeUpdate();
        }
    }
}
