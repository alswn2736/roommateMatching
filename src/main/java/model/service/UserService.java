package model.service;

import java.sql.Connection;
import java.sql.SQLException;

import model.dao.UserDAO;
import model.dto.UserDTO;
import util.DBUtil;

public class UserService {
    private static UserService userService = new UserService();
    
    private UserService() {}
    
    public static UserService getInstance() {
        return userService;
    }
    
    
    // 학번 찾기
    public UserDTO findUser(int studentId) throws SQLException, UserNotFoundException {
        try (Connection conn = DBUtil.getConnection()) {
            UserDAO userDAO = new UserDAO(conn);
            
            UserDTO user = userDAO.selectById(studentId);
            
            if(user == null) {
                throw new UserNotFoundException(studentId + "는 존재하지 않은 학번입니다.");
            }
            return user;
        }
    }
}