package model.dto;

import java.sql.Timestamp;

public class ApplyDTO {
    private int applyId;
    private int applicantId;
    private int postId;
    private String introduction;
    private String status;
    private Timestamp createAt;
    
    public static final String PENDING = "PENDING";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String REJECTED = "REJECTED";
    
    public ApplyDTO() {}
    
    public ApplyDTO(int applyId, int applicantId, int postId, String introduction, String status, Timestamp createAt) {
        super();
        this.applyId = applyId;
        this.applicantId = applicantId;
        this.postId = postId;
        this.introduction = introduction;
        this.status = status;
        this.createAt = createAt;
    }
    
    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "ApplyDTO [applyId=" + applyId + ", applicantId=" + applicantId + ", postId=" + postId
                + ", introduction=" + introduction + ", status=" + status + ", createAt=" + createAt + "]";
    }
}
