package idea.verlif.juststation.global.log;

/**
 * 日志类型
 *
 * @author Verlif
 */
public enum LogType {

    /**
     * 默认
     */
    DEFAULT("DEFAULT"),
    /**
     * 登录
     */
    LOGIN("INSERT"),
    /**
     * 更新
     */
    UPDATE("UPDATE"),
    /**
     * 新增
     */
    INSERT("INSERT"),
    /**
     * 删除
     */
    DELETE("DELETE");

    private final String name;

    LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
