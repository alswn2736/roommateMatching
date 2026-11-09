package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.dto.ChatDTO;

public class ChatDAO {

    private static final String COLS = "msgid, applyid, content, sentat, writerid";

    private ChatDTO makeDTO(ResultSet rs) throws SQLException {
        ChatDTO c = new ChatDTO();
        c.setMsgId(rs.getInt("msgid"));
        c.setApplyId(rs.getInt("applyid"));
        c.setContent(rs.getString("content"));
        c.setSentAt(rs.getTimestamp("sentat"));
        c.setWriterId(rs.getInt("writerid"));
        return c;
    }

    // 새 메시지 번호
    private int nextMsgId(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(msgid), 0) + 1 FROM chat";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public int insert(Connection conn, ChatDTO c) throws SQLException {
        c.setMsgId(nextMsgId(conn));
        if (c.getSentAt() == null) {
            c.setSentAt(new Timestamp(System.currentTimeMillis()));
        }
        String sql = "INSERT INTO chat (" + COLS + ") VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, c.getMsgId());
            pstmt.setInt(2, c.getApplyId());
            pstmt.setString(3, c.getContent());
            pstmt.setTimestamp(4, c.getSentAt());
            pstmt.setInt(5, c.getWriterId());
            return pstmt.executeUpdate();
        }
    }

    // 신청 건의 전체 대화
    public List<ChatDTO> selectByApply(Connection conn, int applyId) throws SQLException {
        List<ChatDTO> list = new ArrayList<ChatDTO>();
        String sql = "SELECT " + COLS + " FROM chat WHERE applyid = ? ORDER BY msgid";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    // !!!! 중요 !!!! Ajax 폴링용... afterMsgId 보다 새로운 메시지만 (처음엔 0) 
    public List<ChatDTO> selectAfter(Connection conn, int applyId, int afterMsgId) throws SQLException {
        List<ChatDTO> list = new ArrayList<ChatDTO>();
        String sql = "SELECT " + COLS + " FROM chat WHERE applyid = ? AND msgid > ? ORDER BY msgid";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applyId);
            pstmt.setInt(2, afterMsgId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(makeDTO(rs));
                }
            }
        }
        return list;
    }

    public int deleteByApply(Connection conn, int applyId) throws SQLException {
        String sql = "DELETE FROM chat WHERE applyid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, applyId);
            return pstmt.executeUpdate();
        }
    }
}