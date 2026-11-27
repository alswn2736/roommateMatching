
package model.dao;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import model.dto.PostDTO;
import util.feed.JDBCUtil;

public class PostManager {

    private static PostManager postMan = new PostManager();

    private PostDAO postDAO = new PostDAO();

    private PostManager() {}

    public static PostManager getInstance() {
        return postMan;
    }

    // 모집 중인 게시글 목록
    public List<PostDTO> findPostList(
            String keyword,
            String[] smoking,
            String[] sleepTime,
            String[] sensitivity,
            String airCon,
            String clean,
            String sort) throws Exception {

        JDBCUtil jdbc = new JDBCUtil();
        
        List<PostDTO> postList;

        try (Connection conn = jdbc.getConnection()) {
            postList = postDAO.getPostList(conn, keyword, smoking, sleepTime, sensitivity, airCon, clean, sort);
        }

        return postList;
    }
    
    // 모집글 상세 조회
    public PostDTO findPost(int postId) throws Exception {
        JDBCUtil jdbc = new JDBCUtil();
        
        try (Connection conn = jdbc.getConnection()) {
            return postDAO.getPost(conn, postId);
        }
    }
    
    // 모집글 등록
    public void createPost(PostDTO post) throws Exception {
        
        JDBCUtil jdbc = new JDBCUtil();
        
        try (Connection conn = jdbc.getConnection()) {
            conn.setAutoCommit(false);

            try {
                post.setStatus("Y");
                post.setCreateAt(new Timestamp(System.currentTimeMillis()));
                
                postDAO.insertPost(conn, post);
                
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    // 모집글 수정
    public void updatePost(PostDTO post) throws Exception {
        JDBCUtil jdbc = new JDBCUtil();
        
        try (Connection conn = jdbc.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                postDAO.updatePost(conn, post);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
    
    // 모집글 삭제
    public void deletePost(int postId, int writerId) throws Exception {
        JDBCUtil jdbc = new JDBCUtil();
        
        try (Connection conn = jdbc.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                postDAO.deletePost(conn, postId, writerId);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
