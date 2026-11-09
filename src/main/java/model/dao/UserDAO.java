package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.dto.UserDTO;

public class UserDAO {

    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsByNickname(String nickname) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public int insert(UserDTO user) throws SQLException {
        String email = user.getEmail();

        int studentid;
        try {
            String id = email.substring(0, email.indexOf("@"));
            studentid = Integer.parseInt(id);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("올바른 학번 형식의 이메일이 아닙니다.", e);
        }

        String sql = "INSERT INTO users "
                   + "(studentid, password, email, namepublic, studentidpublic, majorpublic) "
                   + "VALUES (?, ?, ?, 'N', 'N', 'N')";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentid);
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, email);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                user.setStudentId(studentid);
            }

            return result;
        }
    }

    public UserDTO selectByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return getUser(rs);
                }
            }
        }

        return null;
    }

    public UserDTO selectById(int studentid) throws SQLException {
        String sql = "SELECT * FROM users WHERE studentid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentid);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return getUser(rs);
                }
            }
        }

        return null;
    }

    public int updateProfile(UserDTO user) throws SQLException {
        String sql = "UPDATE users SET nickname = ?, name = ?, major = ?, "
                   + "profileimg = ?, namepublic = ?, studentidpublic = ?, "
                   + "majorpublic = ? WHERE studentid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNickname());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getMajor());
            pstmt.setString(4, user.getProfileImg());
            pstmt.setString(5, user.getNamePublic());
            pstmt.setString(6, user.getStudentIdPublic());
            pstmt.setString(7, user.getMajorPublic());
            pstmt.setInt(8, user.getStudentId());

            return pstmt.executeUpdate();
        }
    }

    public int updateVisibility(UserDTO user) throws SQLException {
        String sql = "UPDATE users SET namepublic = ?, studentidpublic = ?, "
                   + "majorpublic = ? WHERE studentid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNamePublic());
            pstmt.setString(2, user.getStudentIdPublic());
            pstmt.setString(3, user.getMajorPublic());
            pstmt.setInt(4, user.getStudentId());

            return pstmt.executeUpdate();
        }
    }

    public int updateProfileImg(int studentid, String profileimg)
            throws SQLException {
        String sql = "UPDATE users SET profileimg = ? WHERE studentid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, profileimg);
            pstmt.setInt(2, studentid);

            return pstmt.executeUpdate();
        }
    }

    private UserDTO getUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();

        user.setStudentId(rs.getInt("studentid"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setMajor(rs.getString("major"));
        user.setProfileImg(rs.getString("profileimg"));
        user.setNamePublic(rs.getString("namepublic"));
        user.setStudentIdPublic(rs.getString("studentidpublic"));
        user.setMajorPublic(rs.getString("majorpublic"));

        return user;
    }
}
