package idea.verlif.juststation.global.file;

/**
 * 文件域 <br/>
 * 文件域的目的是方便维护，并且可以通过简单的修改来达到对文件的权限管控
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

    /**
     * 文件夹名（请注意格式，例如“test/”，以下格式不允许：“/test/”、“test”）
     */
    private final String Area;

    FileCart(String type) {
        this.Area = type;
    }

    public String getArea() {
        return Area;
    }
}
