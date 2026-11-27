package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import model.dto.UserDTO;
import model.util.auth.AuthDBUtil;
import model.util.auth.AuthMyBatisUtil;

public class UserMyBatisDAO {

    private static final String NAMESPACE =
            "mybatis.mapper.UserMapper.";

    public UserDTO selectByEmail(String email) throws SQLException {

        try (Connection conn = AuthDBUtil.getConnection();
             SqlSession session = AuthMyBatisUtil.getSession(conn)) {

            return session.selectOne(
                    NAMESPACE + "selectByEmail", email);

        } catch (PersistenceException e) {
            throw new SQLException("회원 조회 실패", e);
        }
    }

    public UserDTO selectById(int studentId) throws SQLException {

        try (Connection conn = AuthDBUtil.getConnection();
             SqlSession session = AuthMyBatisUtil.getSession(conn)) {

            return session.selectOne(
                    NAMESPACE + "selectById", studentId);

        } catch (PersistenceException e) {
            throw new SQLException("회원 조회 실패", e);
        }
    }
}