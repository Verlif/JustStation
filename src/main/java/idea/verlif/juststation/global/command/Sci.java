package idea.verlif.juststation.global.command;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指令标志
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/24 14:20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Sci {

    /**
     * 命令标识, 用于标记命令 <br/>
     * 区分大小写
     *
     * @return 该命令可用的所有命令标识
     */
    String[] key();

    /**
     * 指令描述
     *
     * @return 介绍指令用法及功能
     */
    String description() default "";
}
