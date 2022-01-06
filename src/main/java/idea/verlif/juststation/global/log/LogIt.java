package idea.verlif.juststation.global.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/26 14:20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogIt {

    /**
     * 日志类型
     */
    LogType type() default LogType.DEFAULT;

    /**
     * 记录附加信息
     */
    String message();

    /**
     * 日志等级
     */
    LogLevel level() default LogLevel.INFO;

    /**
     * 日志处理类
     */
    Class<? extends ApiLogHandler> handler() default ApiLogManager.ApiLogHandlerAto.class;

}
