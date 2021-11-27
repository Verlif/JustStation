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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogIt {

    /**
     * 日志类型
     */
    Type type();

    /**
     * 记录附加信息
     */
    String message();

    /**
     * 日志等级
     */
    LogLevel level() default LogLevel.DEBUG;

    enum Type {
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

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
