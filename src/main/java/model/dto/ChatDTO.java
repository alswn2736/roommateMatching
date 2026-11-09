package model.dto;
import java.sql.Timestamp;

public class ChatDTO {
    private int msgId;
    private int applyId;
    private String content;
    private Timestamp sentAt;
    private int writerId;
    
    public ChatDTO() {}
    
    public ChatDTO(int msgId, int applyId, String content, Timestamp sentAt, int writerId) {
        super();
        this.msgId = msgId;
        this.applyId = applyId;
        this.content = content;
        this.sentAt = sentAt;
        this.writerId = writerId;
    }

    public int getMsgId() { return msgId; }
    public void setMsgId(int msgId) { this.msgId = msgId; }

    public int getApplyId() { return applyId; }
    public void setApplyId(int applyId) { this.applyId = applyId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public java.sql.Timestamp getSentAt() { return sentAt; }
    public void setSentAt(java.sql.Timestamp sentAt) { this.sentAt = sentAt; }

    public int getWriterId() { return writerId; }
    public void setWriterId(int writerId) { this.writerId = writerId; }
    
    @Override
    public String toString() {
        return "ChatDTO [msgId=" + msgId + ", applyId=" + applyId + ", content=" + content + ", sentAt=" + sentAt
                + ", writerId=" + writerId + "]";
    }
}
