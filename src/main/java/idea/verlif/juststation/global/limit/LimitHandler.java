package idea.verlif.juststation.global.limit;

/**
 * 限制处理类
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/30 8:57
 */
public interface LimitHandler {

    /**
     * 访问回调，在方法调用前触发
     *
     * @param key 限制Key
     * @return 是否允许访问
     */
    boolean arrived(String key);
}
