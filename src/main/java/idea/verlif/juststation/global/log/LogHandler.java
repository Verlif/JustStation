package idea.verlif.juststation.global.log;

import java.lang.reflect.Method;

/**
 * 日志处理器
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/26 14:28
 */
public interface LogHandler {

    /**
     * 日志记录
     *
     * @param method 标记的方法
     * @param logIt  标记的注解
     */
    void onLog(Method method, LogIt logIt);

    /**
     * 方法完成时
     *
     * @param method 标记的方法
     * @param logIt  标记的注解
     * @param o 返回值
     */
    void onReturn(Method method, LogIt logIt, Object o);
}
