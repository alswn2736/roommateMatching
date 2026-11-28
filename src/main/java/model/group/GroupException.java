package model.group;

public class GroupException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;

    public GroupException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
