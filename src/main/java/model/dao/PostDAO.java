package model.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.JDBCUtil;
import model.dto.PostDTO;

public class PostDAO {
    private JDBCUtil jdbcUtil = null;   // JDBCUtil 필드 선언
    
    public PostDAO() {
        jdbcUtil = new JDBCUtil();
    }
    
    //목록 전체 조회
    public List<PostDTO> getPostList() {
        List<PostDTO> postList = new ArrayList<PostDTO>();
        
        StringBuffer query = new StringBuffer();
        query.append("SELECT postId, writerId, title, capacity, acceptedCount, ");
        query.append("kwSmoking, kwSleepTime, kwSensetivity, kwAircon, kwClean, status, content, createAt ");
        query.append("FROM post ");
        query.append("ORDER BY createAt DESC");
        
        jdbcUtil.setSql(query.toString());
        
        try {
            ResultSet rs = jdbcUtil.executeQuery();
            
            while(rs.next()) {
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
                
                postList.add(post);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close();
        }
        
        return postList;
    }
    
    //모집글 상세 조회
    public PostDTO getPost(int postId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT postId, writerId, title, capacity, acceptedCount, ");
        query.append("kwSmoking, kwSleepTime, kwSensetivity, kwAircon, kwClean, status, content, createAt ");
        query.append("FROM post ");
        query.append("WHERE postId = ?");
        
        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] {postId});
        
        PostDTO post = new PostDTO();
        
        try {
            ResultSet rs = jdbcUtil.executeQuery();
            
            if(rs.next()) {
                post.setPostId(rs.getInt("postId"));
                post.setWriterId(rs.getInt("writerid"));
                post.setTitle(rs.getString("title"));
                post.setCapacity(rs.getInt("capacity"));
                post.setAcceptedCount(rs.getInt("acceptedcount"));
                post.setKwSmoking(rs.getString("kwsmoking"));
                post.setKwSleepTime(rs.getString("kwsleeptime"));
                post.setKwSensitivity(rs.getString("kwsensitivity"));
                post.setKwAircon(rs.getString("kwaircon"));
                post.setKwClean(rs.getString("kwclean"));
                post.setStatus(rs.getString("status"));
                post.setContent(rs.getString("content"));
                post.setCreateAt(rs.getTimestamp("createat"));
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close();
        }
        
        return post;
    }
    
    //단어 검색
    public List<PostDTO> searchPostList(String keyword) {
        List<PostDTO> postList = new ArrayList<PostDTO>();
        
        StringBuffer query = new StringBuffer();
        query.append("SELECT postId, writerId, title, capacity, acceptedCount, ");
        query.append("kwSmoking, kwSleepTime, kwSensetivity, kwAircon, kwClean, status, content, createAt ");
        query.append("FROM post ");
        query.append("WHERE title LIKE ? OR content LIKE ?");
        query.append("ORDER BY createAt DESC");
        
        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] {"%" + keyword + "%", "%" + keyword + "%"});
        
        try {
            ResultSet rs = jdbcUtil.executeQuery();
            
            while(rs.next()) {
                PostDTO post = new PostDTO();

                post.setPostId(rs.getInt("postId"));
                post.setWriterId(rs.getInt("writerId"));
                post.setTitle(rs.getString("title"));
                post.setCapacity(rs.getInt("capacity"));
                post.setAcceptedCount(rs.getInt("acceptedCount"));
                post.setKwSmoking(rs.getString("kwSmoking"));
                post.setKwSleepTime(rs.getString("kwSleepTime"));
                post.setKwSensitivity(rs.getString("kwSensitivity"));
                post.setKwAircon(rs.getString("kwAircon"));
                post.setKwClean(rs.getString("kwClean"));
                post.setStatus(rs.getString("status"));
                post.setContent(rs.getString("content"));
                post.setCreateAt(rs.getTimestamp("createAt"));
                
                postList.add(post);
            }
            rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            jdbcUtil.close();
        }
        
        return postList;
    }
    
    //모집글 등록
    public int insertPost(PostDTO post) {
        StringBuffer query = new StringBuffer();

        query.append("INSERT INTO post (");
        query.append("postId, writerId, title, capacity, acceptedCount, ");
        query.append("kwSmoking, kwSleepTime, kwSensitivity, kwAircon, kwClean, status, content, createAt");
        query.append(") VALUES (");
        query.append("POST_POSTID_SEQ.NEXTVAL, ?, ?, ?, ?, ");
        query.append("?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP)");
        
        jdbcUtil.setSqlAndParameters(query.toString(),
                new Object[] {post.getWriterId(), post.getTitle(), post.getCapacity(), post.getAcceptedCount(), 
                        post.getKwSmoking(), post.getKwSleepTime(), post.getKwSensitivity(), post.getKwAircon(),
                        post.getKwClean(), post.getStatus(), post.getContent()});
        try {
            int result = jdbcUtil.executeUpdate();
            jdbcUtil.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            jdbcUtil.rollback();
            return 0;
        } finally {
            jdbcUtil.close();
        }
    }
    
  //모집글 수정
    public int updatePost(PostDTO post) {
        StringBuffer query = new StringBuffer();

        query.append("UPDATE post SET ");
        query.append("title = ?, capacity = ?, kwSmoking = ?, kwSleepTime = ?, kwSensitivity = ?, ");
        query.append("kwAircon = ?, kwClean = ?, status = ?, content = ? ");
        query.append("WHERE postId = ? AND writerId = ?");
        
        jdbcUtil.setSqlAndParameters(query.toString(),
                new Object[] {post.getWriterId(), post.getTitle(), post.getCapacity(), post.getKwSmoking(), post.getKwSleepTime(),
                        post.getKwSensitivity(), post.getKwAircon(), post.getKwClean(), post.getStatus(), post.getContent(), 
                        post.getPostId(), post.getWriterId()});
        try {
            int result = jdbcUtil.executeUpdate();
            jdbcUtil.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            jdbcUtil.rollback();
            return 0;
        } finally {
            jdbcUtil.close();
        }
    }
    
  //모집글 수정
    public int deletePost(int postId, int writerId) {
        StringBuffer query = new StringBuffer();

        query.append("DELETE FROM post ");
        query.append("WHERE postId = ? AND writerId = ?");
        
        jdbcUtil.setSqlAndParameters(query.toString(), new Object[] {postId, writerId});
        try {
            int result = jdbcUtil.executeUpdate();
            jdbcUtil.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            jdbcUtil.rollback();
            return 0;
        } finally {
            jdbcUtil.close();
        }
    }
}