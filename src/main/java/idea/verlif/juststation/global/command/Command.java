package idea.verlif.juststation.global.command;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 指令接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/15 10:11
 */
public interface Command {

    /**
     * 命令的执行内容
     * 此方法运行于主线程
     *
     * @param params 指令后的
     */
    void run(String[] params);

    /**
     * 参数说明
     * @return 指令可用参数说明
     */
    default String[] params() {
        return new String[]{};
    }

    /**
     * 指令注释, 便于添加指令名称
     */
    @Documented
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Component
    @interface CommandInfo {
        /**
         * 命令标识, 用于标记命令
         *
         * @return 该命令可用的所有命令标识
         */
        String[] key() default "command";

        /**
         * 指令描述
         *
         * @return 介绍指令用法及功能
         */
        String description() default "";
    }
}