package model.dto.mypage;

import java.sql.Timestamp;

import model.dto.ApplyDTO;

/** 신청자 관리 화면: 신청(apply) 1건 + 신청자(users) 정보 */
public class ApplicantDTO extends ProfileView {

    private int applyId;
    private int postId;
    private String introduction;
    private String status;
    private Timestamp createAt;

    public boolean isPending() { return ApplyDTO.PENDING.equals(status); }
    public boolean isAccepted() { return ApplyDTO.ACCEPTED.equals(status); }

    public int getApplyId() { return applyId; }
    public void setApplyId(int applyId) { this.applyId = applyId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreateAt() { return createAt; }
    public void setCreateAt(Timestamp createAt) { this.createAt = createAt; }
}
