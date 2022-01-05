package idea.verlif.juststation.global.task;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/5 10:03
 */
public enum TaskType {

    /**
     * 表达式
     */
    CRON,

    /**
     * 重复任务，在上一次任务结束后开始计时
     */
    REPEAT_DELAY,

    /**
     * 重复任务，在上一次任务开始后开始计时
     */
    REPEAT_RATE,

    /**
     * 单次延时任务
     */
    DELAY,
}
