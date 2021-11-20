package idea.verlif.juststation.global.notice;

/**
 * 通知标志
 *
 * @author Verlif
 */
public enum NoticeTag {

    /**
     * 邮件通知
     */
    EMAIL("email");

    private final String tag;

    NoticeTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
