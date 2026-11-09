package model.dto;
import java.sql.Timestamp;

public class PostDTO {
    private int postId;
    private int writerId;
    private String title;
    private int capacity;
    private int acceptedCount;
    private String kwSmoking;
    private String kwSleepTime;
    private String kwSensitivity;
    private String kwAirCon;
    private String kwClean;
    private String status;
    private String content;
    private Timestamp createAt;

    public PostDTO() {}
    
    public PostDTO(int postId, int writerId, String title, int capacity, int acceptedCount, String kwSmoking,
            String kwSleepTime, String kwSensitivity, String kwAirCon, String kwClean, String status, String content,
            Timestamp createAt) {
        super();
        this.postId = postId;
        this.writerId = writerId;
        this.title = title;
        this.capacity = capacity;
        this.acceptedCount = acceptedCount;
        this.kwSmoking = kwSmoking;
        this.kwSleepTime = kwSleepTime;
        this.kwSensitivity = kwSensitivity;
        this.kwAirCon = kwAirCon;
        this.kwClean = kwClean;
        this.status = status;
        this.content = content;
        this.createAt = createAt;
    }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getWriterId() { return writerId; }
    public void setWriterId(int writerId) { this.writerId = writerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getAcceptedCount() { return acceptedCount; }
    public void setAcceptedCount(int acceptedCount) { this.acceptedCount = acceptedCount; }

    public String getKwSmoking() { return kwSmoking; }
    public void setKwSmoking(String kwSmoking) { this.kwSmoking = kwSmoking; }

    public String getKwSleepTime() { return kwSleepTime; }
    public void setKwSleepTime(String kwSleepTime) { this.kwSleepTime = kwSleepTime; }

    public String getKwSensitivity() { return kwSensitivity; }
    public void setKwSensitivity(String kwSensitivity) { this.kwSensitivity = kwSensitivity; }

    public String getKwAirCon() { return kwAirCon; }
    public void setKwAirCon(String kwAirCon) { this.kwAirCon = kwAirCon; }

    public String getKwClean() { return kwClean; }
    public void setKwClean(String kwClean) { this.kwClean = kwClean; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreateAt() { return createAt; }
    public void setCreateAt(Timestamp createAt) { this.createAt = createAt; }

}
