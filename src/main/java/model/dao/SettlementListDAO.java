package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.dto.SettlementListDTO;
import java.math.BigDecimal; //BigDecimal은 숫자를 오차 없이 다루기 위한 자료형

public class SettlementListDAO {

    private SettlementListDTO makeDTO(ResultSet rs) throws SQLException {
        SettlementListDTO dto = new SettlementListDTO();
        dto.setListId(rs.getInt("listid"));
        if (rs.wasNull()) {
            dto.setListId(0);
        }
        dto.setGroupId(rs.getInt("groupid"));
        if (rs.wasNull()) {
            dto.setGroupId(0);
        }
        dto.setMonth(rs.getString("month"));
        dto.setItemName(rs.getString("itemname"));
        dto.setPayerId(rs.getInt("payerid"));
        if (rs.wasNull()) {
            dto.setPayerId(0);
        }
        dto.setListPrice(rs.getBigDecimal("listprice"));
        return dto;
    }

    public List<SettlementListDTO> findByGroup(Connection conn, Long groupId) throws SQLException {
        List<SettlementListDTO> list = new ArrayList<SettlementListDTO>();
        String sql = "SELECT listid, groupid, month, itemname, payerid, listprice FROM settlementlist WHERE groupid = ? ORDER BY listid, groupid, month";

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

    public List<SettlementListDTO> findByGroupAndMonth(Connection conn, Long groupId, String month) throws SQLException {
        List<SettlementListDTO> list = new ArrayList<SettlementListDTO>();
        String sql = "SELECT listid, groupid, month, itemname, payerid, listprice FROM settlementlist WHERE groupid = ? AND month = ? ORDER BY listid, groupid, month";

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

    public int insert(Connection conn, SettlementListDTO dto) throws SQLException {
        if (dto.getListId() == 0) {
            throw new IllegalArgumentException("listid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMonth() == null) {
            throw new IllegalArgumentException("month 값이 필요합니다.");
        }
        if (dto.getListPrice() != null && dto.getListPrice().stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException("금액은 정수로 입력해 주세요.");
        }
        String sql = "INSERT INTO settlementlist (listid, groupid, month, itemname, payerid, listprice) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dto.getListId());
            pstmt.setLong(2, dto.getGroupId());
            pstmt.setString(3, dto.getMonth());
            pstmt.setString(4, dto.getItemName());
            if (dto.getPayerId() == 0) {
                pstmt.setNull(5, Types.NUMERIC);
            } else {
                pstmt.setLong(5, dto.getPayerId());
            }
            pstmt.setBigDecimal(6, dto.getListPrice());
            return pstmt.executeUpdate();
        }
    }

    public int update(Connection conn, SettlementListDTO dto) throws SQLException {
        if (dto.getListId() == 0) {
            throw new IllegalArgumentException("listid 값이 필요합니다.");
        }
        if (dto.getGroupId() == 0) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (dto.getMonth() == null) {
            throw new IllegalArgumentException("month 값이 필요합니다.");
        }
        if (dto.getListPrice() != null && dto.getListPrice().stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException("금액은 정수로 입력해 주세요.");
        }
        String sql = "UPDATE settlementlist SET itemname = ?, payerid = ?, listprice = ? WHERE listid = ? AND groupid = ? AND month = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getItemName());
            if (dto.getPayerId() == 0) {
                pstmt.setNull(2, Types.NUMERIC);
            } else {
                pstmt.setLong(2, dto.getPayerId());
            }
            pstmt.setBigDecimal(3, dto.getListPrice());
            pstmt.setLong(4, dto.getListId());
            pstmt.setLong(5, dto.getGroupId());
            pstmt.setString(6, dto.getMonth());
            return pstmt.executeUpdate();
        }
    }

    public int delete(Connection conn, Long listId, Long groupId, String month) throws SQLException {
        if (listId == null) {
            throw new IllegalArgumentException("listid 값이 필요합니다.");
        }
        if (groupId == null) {
            throw new IllegalArgumentException("groupid 값이 필요합니다.");
        }
        if (month == null) {
            throw new IllegalArgumentException("month 값이 필요합니다.");
        }
        String sql = "DELETE FROM settlementlist WHERE listid = ? AND groupid = ? AND month = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, listId);
            pstmt.setLong(2, groupId);
            pstmt.setString(3, month);
            return pstmt.executeUpdate();
        }
    }

    public BigDecimal getTotalByMonth(Connection conn, Long groupId, String month) throws SQLException {
        String sql = "SELECT SUM(listprice) AS total FROM settlementlist WHERE groupid = ? AND month = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (groupId == null) {
                pstmt.setNull(1, Types.NUMERIC);
            } else {
                pstmt.setLong(1, groupId);
            }
            pstmt.setString(2, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    if (total != null) {
                        return total;
                    }
                }
            }
        }
        return BigDecimal.ZERO;
    }
}