package model.dto.mypage;

import java.util.ArrayList;
import java.util.List;

import model.dto.PostDTO;

/** 신청자 관리 화면: 내가 쓴 모집글 1개 + 신청자 목록 */
public class MyPostDTO {

    /** 모집이 끝난 글의 status 값 (모집완료) */
    public static final String CLOSED = "CLOSED";

    private PostDTO post;
    private List<ApplicantDTO> applicants = new ArrayList<ApplicantDTO>();

    public MyPostDTO() {}

    public MyPostDTO(PostDTO post, List<ApplicantDTO> applicants) {
        this.post = post;
        if (applicants != null) {
            this.applicants = applicants;
        }
    }

    /** 작성자 본인을 포함한 정원이 다 찼는지 (capacity = 방 인원) */
    public static boolean isFull(PostDTO p) {
        return p.getCapacity() > 0 && p.getAcceptedCount() + 1 >= p.getCapacity();
    }

    public static boolean isRecruiting(PostDTO p) {
        String s = p.getStatus();
        boolean closedStatus = s != null
                && (CLOSED.equalsIgnoreCase(s) || "DONE".equalsIgnoreCase(s)
                    || "COMPLETE".equalsIgnoreCase(s) || "모집완료".equals(s));
        return !closedStatus && !isFull(p);
    }

    public boolean isRecruiting() {
        return post != null && isRecruiting(post);
    }

    public int getPendingCount() {
        int n = 0;
        for (ApplicantDTO a : applicants) {
            if (a.isPending()) n++;
        }
        return n;
    }

    public String getRoomLabel() {
        return post == null || post.getCapacity() <= 0 ? "인원 미정" : post.getCapacity() + "인실 희망";
    }

    public PostDTO getPost() { return post; }
    public void setPost(PostDTO post) { this.post = post; }

    public List<ApplicantDTO> getApplicants() { return applicants; }
    public void setApplicants(List<ApplicantDTO> applicants) { this.applicants = applicants; }
}
