package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dto.PostDTO;

public class PostDAO {
    
    private static final String COLS = "postid, writerid, title, capacity, acceptedcount, " +
            "kWsmoking, kwsleeptime, kwsensivity, kwaircon, kwclean, " +
            "status, content, createat";
    
    // 새 모집글 번호 (현재 최대값 + 1)
    private int nextPostId(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(postid), 0) + 1 FROM post";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
    
    private PostDTO makeDTO(ResultSet rs) throws SQLException {
        PostDTO post = new PostDTO();
        
        post.setPostId(rs.getInt("postId"));
        post.setWriterId(rs.getInt("writerId"));
        post.setTitle(rs.getString("title"));
        post.setCapacity(rs.getInt("capacity"));
        post.setAcceptedCount(rs.getInt("acceptedCount"));
        post.setKwSmoking(rs.getString("kwSmoking"));
        post.setKwSleepTime(rs.getString("kwSleepTime"));
        post.setKwSensitivity(rs.getString("kwSensitivity"));
        post.setKwAirCon(rs.getString("kwAircon"));
        post.setKwClean(rs.getString("kwClean"));
        post.setStatus(rs.getString("status"));
        post.setContent(rs.getString("content"));
        post.setCreateAt(rs.getTimestamp("createAt"));
        
        return post;
    }
    
    //모집글 전체 조회
    public List<PostDTO> getPostList(Connection conn) throws SQLException {
        List<PostDTO> postList = new ArrayList<PostDTO>();
        
        String query = "SELECT " + COLS +" FROM post ORDER BY createAt DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query); 
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                postList.add(makeDTO(rs));
            }
        }
        
        return postList;
    }
    
    //모집글 상세 조회(없으면 null)
    public PostDTO getPost(Connection conn, int postId) throws SQLException {
        String query = "SELECT " + COLS + " FROM post WHERE postid = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            
            try (ResultSet rs = pstmt.executeQuery()){
                return rs.next() ? makeDTO(rs) : null;
            }
        }
    }
    
    //제목 및 내용 키워드 검색
    public List<PostDTO> searchPostList(Connection conn, String keyword) throws SQLException {
        
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }
        
        List<PostDTO> postList = new ArrayList<PostDTO>();
        
        String query = "SELECT " + COLS + " FROM post " + 
                "WHERE title LIKE ? OR content LIKE ? " +
                "ORDER BY createAt DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1,  "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    postList.add(makeDTO(rs));
                }
            }
        }
        
        return postList;
    }
    
    //모집글 등록
    public int insertPost(Connection conn, PostDTO post) throws SQLException {
        
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }

        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        
        post.setPostId(nextPostId(conn));

        String sql = "INSERT INTO post (" + COLS + ") " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, post.getPostId());
            pstmt.setInt(2, post.getWriterId());
            pstmt.setString(3, post.getTitle());
            pstmt.setInt(4, post.getCapacity());
            pstmt.setInt(5, post.getAcceptedCount());
            pstmt.setString(6, post.getKwSmoking());
            pstmt.setString(7, post.getKwSleepTime());
            pstmt.setString(8, post.getKwSensitivity());
            pstmt.setString(9, post.getKwAirCon());
            pstmt.setString(10, post.getKwClean());
            pstmt.setString(11, post.getStatus());
            pstmt.setString(12, post.getContent());
            pstmt.setTimestamp(13, post.getCreateAt());

            return pstmt.executeUpdate();
        }
    }
    
    //모집글 수정
    public int updatePost(Connection conn, PostDTO post) throws SQLException {
        String query = "UPDATE post " + 
                "SET title = ?, capacity = ?, kwSmoking = ?, kwSleepTime = ?, " + 
                "kwSensitivity = ?, kwAircon = ?, kwClean = ?, status = ?, content = ? " + 
                "WHERE postId = ? AND writerId = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, post.getTitle());
            pstmt.setInt(2, post.getCapacity());
            pstmt.setString(3, post.getKwSmoking());
            pstmt.setString(4, post.getKwSleepTime());
            pstmt.setString(5, post.getKwSensitivity());
            pstmt.setString(6, post.getKwAirCon());
            pstmt.setString(7, post.getKwClean());
            pstmt.setString(8, post.getStatus());
            pstmt.setString(9, post.getContent());
            pstmt.setInt(10, post.getPostId());
            pstmt.setInt(11, post.getWriterId());
            
            return pstmt.executeUpdate();
        }
    }
    
    //모집글 삭제
    public int deletePost(Connection conn, int postId, int writerId) throws SQLException{
        String query = "DELETE FROM post WHERE postId = ? AND writerId = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, postId);
            pstmt.setInt(2, writerId);
            
            return pstmt.executeUpdate();
        }
    }
}