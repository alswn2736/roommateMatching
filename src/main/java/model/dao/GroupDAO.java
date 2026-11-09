package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.GroupDTO;

public class GroupDAO {

    private GroupDTO makeDTO(ResultSet rs) throws SQLException {
        GroupDTO dto = new GroupDTO();
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setCapacity(rs.getInt("capacity"));
        if (rs.wasNull()) {
            dto.setCapacity(0);
        }
        dto.setAcademicYear(rs.getInt("academicyear"));
        if (rs.wasNull()) {
            dto.setAcademicYear(0);
        }
        dto.setSemester(rs.getInt("semester"));
        if (rs.wasNull()) {
            dto.setSemester(0);
        }
        dto.setStatus(rs.getString("status"));
        return dto;
    }

    public List<GroupDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<GroupDTO> list = new ArrayList<GroupDTO>();
        String sql = "SELECT groupid, capacity, academicyear, semester, status FROM groups WHERE groupid = ? ORDER BY groupid";

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

    public int insert(Connection conn, GroupDTO dto) throws SQLException {
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "INSERT INTO groups (groupid, capacity, academicyear, semester, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getGroupId());
            if (dto.getCapacity() == 0) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setInt(2, dto.getCapacity());
            }
            if (dto.getAcademicYear() == 0) {
                pstmt.setNull(3, Types.NUMERIC);
            } else {
                pstmt.setInt(3, dto.getAcademicYear());
            }
            if (dto.getSemester() == 0) {
                pstmt.setNull(4, Types.NUMERIC);
            } else {
                pstmt.setInt(4, dto.getSemester());
            }
            pstmt.setString(5, dto.getStatus());
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, GroupDTO dto) throws SQLException {
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "UPDATE groups SET capacity = ?, academicyear = ?, semester = ?, status = ? WHERE groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (dto.getCapacity() == 0) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setInt(1, dto.getCapacity());
            }
            if (dto.getAcademicYear() == 0) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setInt(2, dto.getAcademicYear());
            }
            if (dto.getSemester() == 0) {
                pstmt.setNull(3, Types.NUMERIC);
            } else {
                pstmt.setInt(3, dto.getSemester());
            }
            pstmt.setString(4, dto.getStatus());
            pstmt.setLong(5, dto.getGroupId());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long groupId) throws SQLException {
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        String sql = "DELETE FROM groups WHERE groupid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            return pstmt.executeUpdate();
        }
    }
}