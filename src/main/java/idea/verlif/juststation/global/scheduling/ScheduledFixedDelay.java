package idea.verlif.juststation.global.scheduling;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 14:11
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledFixedDelay {

    /**
     * 定时任务名称（唯一），默认类名
     */
    String value() default "";

    /**
     * 间隔时长（单位：毫秒）
     */
    long interval();

    /**
     * 延迟时间（单位：毫秒）
     */
    long delay() default 0L;

    /**
     * 时间单位（默认毫秒）
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
