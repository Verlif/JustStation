package idea.verlif.juststation.global.scheduling;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 14:11
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledCron {

    /**
     * 定时任务名称（唯一），默认类名
     */
    String value() default "";

    /**
     * cron表达式
     */
    String cron();
}
