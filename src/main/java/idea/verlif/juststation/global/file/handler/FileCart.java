package idea.verlif.juststation.global.file.handler;

/**
 * 文件域 <br/>
 * 文件域的目的是方便维护，并且可以通过简单的修改来达到对文件的权限管控 <br/>
 * 建议文件相关的路径定义在文件域中
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 10:44
 */
public enum FileCart {

    /**
     * 测试路径
     */
    TEST("test/");

    private static final String SPLIT = "/";

    /**
     * 文件夹名
     */
    private final String Area;

    FileCart(String type) {
        StringBuilder sb = new StringBuilder();
        for (String s : type.split(SPLIT)) {
            if (s.length() > 0) {
                sb.append(s).append(SPLIT);
            }
        }
        this.Area = sb.toString();
    }

    public String getArea() {
        return Area;
    }
}
