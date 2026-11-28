package model.group;

public final class GroupPolicy {

    public static final String PENDING = "PENDING";

    public static final String APPROVED = "APPROVED";

    public static final String REJECTED = "REJECTED";

    private GroupPolicy() {
    }

    public static String text(String value, int max, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new GroupException(400, label + "을 입력해 주세요.");
        }
        String result = value.trim();
        if (result.codePointCount(0, result.length()) > max) {
            throw new GroupException(400, label + "이 너무 깁니다.");
        }
        return result;
    }

    public static String yesNo(String value) {
        if (!"Y".equals(value) && !"N".equals(value)) {
            throw new GroupException(400, "동의 여부를 선택해 주세요.");
        }
        return value;
    }
}
