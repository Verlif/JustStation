package idea.verlif.juststation.global.file;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 10:44
 */
public enum FileCart {
    /**
     * 测试路径
     */
    TEST("/test");

    private final String type;

    FileCart(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
