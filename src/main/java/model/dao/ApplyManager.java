
package model.dao;

import java.sql.Connection;

import model.dto.ApplyDTO;
import util.feed.JDBCUtil;

public class ApplyManager {

    private static ApplyManager applyMan = new ApplyManager();

    private ApplyDAO applyDAO = new ApplyDAO();

    private ApplyManager() {}

    public static ApplyManager getInstance() {
        return applyMan;
    }

    // 모집 신청 등록
    public void createApply(ApplyDTO apply) throws Exception {

        JDBCUtil jdbc = new JDBCUtil();

        try (Connection conn = jdbc.getConnection()) {

            conn.setAutoCommit(false);

            try {
                // 중복 신청 확인
                boolean exists = applyDAO.existsByPostAndApplicant(
                        conn, apply.getPostId(), apply.getApplicantId());

                if (exists) {
                    throw new IllegalArgumentException("이미 신청한 모집글입니다.");
                }

                // 신청 정보 저장
                applyDAO.insert(conn, apply);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
