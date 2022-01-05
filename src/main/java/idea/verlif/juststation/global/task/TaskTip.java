package idea.verlif.juststation.global.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/5 10:03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskTip {

    /**
     * 定时任务名称（唯一），默认类名
     */
    String value() default "";

    /**
     * 任务类型
     */
    TaskType type();

    /**
     * cron表达式 <br/>
     * 可用的type有：
     * <ul>
     *     <li>{@link TaskType#CRON}</li>
     * </ul>
     */
    String cron() default "";

    /**
     * 间隔时长（单位：毫秒） <br/>
     * 可用的type有：
     * <ul>
     *     <li>{@link TaskType#REPEAT_RATE}</li>
     *     <li>{@link TaskType#REPEAT_DELAY}</li>
     * </ul>
     */
    long interval() default 0L;

    /**
     * 延迟时间（单位：毫秒） <br/>
     * 可用的type有：
     * <ul>
     *     <li>{@link TaskType#REPEAT_RATE}</li>
     *     <li>{@link TaskType#REPEAT_DELAY}</li>
     *     <li>{@link TaskType#DELAY}</li>
     * </ul>
     */
    long delay() default 0L;

    /**
     * 时间单位，影响{@link #delay()}和{@link #interval()}（默认毫秒） <br/>
     * 可用的type有：
     * <ul>
     *     <li>{@link TaskType#REPEAT_RATE}</li>
     *     <li>{@link TaskType#REPEAT_DELAY}</li>
     *     <li>{@link TaskType#DELAY}</li>
     * </ul>
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
