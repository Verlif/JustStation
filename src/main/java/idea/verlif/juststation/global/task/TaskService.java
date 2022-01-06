package idea.verlif.juststation.global.task;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * 任务服务。<br/>
 * 任务服务会在启动时加载，在启动结束后开始执行。
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 11:46
 */
@Component
public class TaskService implements ApplicationRunner {

    /**
     * 待添加的任务表
     */
    private final ConcurrentHashMap<String, Runnable> taskMap;
    /**
     * 可重复任务Map，用于控制任务进度
     */
    private final ConcurrentHashMap<String, ScheduledFuture<?>> futureMap;
    /**
     * 是否在添加时直接执行定时任务
     */
    private boolean ready;

    private final ThreadPoolTaskScheduler schedule;
    private final TaskConfig config;

    public TaskService(
            @Autowired ApplicationContext context,
            @Autowired ThreadPoolTaskScheduler schedule,
            @Autowired TaskConfig taskConfig
    ) {
        this.schedule = schedule;
        this.config = taskConfig;

        taskMap = new ConcurrentHashMap<>();
        futureMap = new ConcurrentHashMap<>();
        ready = false;

        Map<String, Runnable> map = context.getBeansOfType(Runnable.class);
        for (Runnable runnable : map.values()) {
            insert(runnable);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        synchronized (taskMap) {
            for (Runnable runnable : taskMap.values()) {
                schedule(null, runnable);
            }
            taskMap.clear();

            StringBuilder sb = new StringBuilder();
            Enumeration<String> enumeration = futureMap.keys();
            while (enumeration.hasMoreElements()) {
                sb.append(enumeration.nextElement()).append(", ");
            }
            PrintUtils.print(Level.INFO, "already loaded repeatable tasks with " + futureMap.size() + " - " + sb.substring(0, sb.length() - 2));
            ready = true;
        }
    }

    /**
     * 获取任务调度器
     *
     * @return 服务内部的调度器
     */
    public ThreadPoolTaskScheduler getSchedule() {
        return schedule;
    }

    /**
     * 添加定时任务 <br/>
     * 添加的任务需要带有{@link TaskTip}注解
     *
     * @param name     任务名称
     * @param runnable 任务对象
     */
    public synchronized void insert(String name, Runnable runnable) {
        synchronized (taskMap) {
            if (ready) {
                schedule(name, runnable);
            } else {
                taskMap.put(name, runnable);
            }
        }
    }

    /**
     * 添加定时任务 <br/>
     * 添加的任务需要带有{@link TaskTip}注解
     *
     * @param runnable 任务对象
     */
    public synchronized void insert(Runnable runnable) {
        synchronized (taskMap) {
            if (ready) {
                schedule(null, runnable);
            } else {
                taskMap.put(runnable.getClass().getSimpleName(), runnable);
            }
        }
    }

    /**
     * 执行任务
     *
     * @param runnable 任务对象
     */
    public void execute(Runnable runnable) {
        schedule.execute(runnable);
    }

    /**
     * 定时执行单次任务
     *
     * @param runnable  任务对象
     * @param startTime 任务开始时间
     */
    public void execute(Runnable runnable, Date startTime) {
        schedule.schedule(runnable, startTime);
    }

    /**
     * 延时执行任务
     *
     * @param runnable 任务对象
     * @param delay    延时时间（单位毫秒）
     */
    public void delay(Runnable runnable, long delay) {
        schedule.schedule(runnable, new Date(System.currentTimeMillis() + delay));
    }

    /**
     * 延时执行任务
     *
     * @param runnable 任务对象
     * @param delay    延时时间
     * @param unit     延时时间单位
     */
    public void delay(Runnable runnable, long delay, TimeUnit unit) {
        delay(runnable, unit.toMillis(delay));
    }

    /**
     * 取消定时任务
     *
     * @param name 任务名称
     * @return 是否取消成功
     */
    public synchronized boolean cancel(String name) {
        ScheduledFuture<?> future = futureMap.get(name);
        if (future == null) {
            return false;
        }
        return future.cancel(true);
    }

    /**
     * 添加定时任务
     *
     * @param defaultName 任务名称；null则使用注解提供的名称规则
     * @param runnable    任务对象
     */
    private void schedule(String defaultName, Runnable runnable) {
        Class<?> cl = runnable.getClass();
        TaskTip tip = cl.getAnnotation(TaskTip.class);
        // 检测注解是否存在
        if (tip == null) {
            PrintUtils.print(Level.WARNING,
                    "runnable [" + cl.getName() + "] lack annotation TaskComponent, it can not been loaded!");
            return;
        }
        String name = defaultName != null ? defaultName : tip.value().length() == 0 ? cl.getSimpleName() : tip.value();
        // 检测任务是否重复
        if (futureMap.containsKey(name)) {
            PrintUtils.print(Level.WARNING, "already exist task " + name + "!!!");
            return;
        }
        // 检测任务是否可添加
        if (config.isAllowed(name)) {
            switch (tip.type()) {
                case CRON: {
                    if (tip.cron().length() == 0) {
                        PrintUtils.print(Level.WARNING, "can not load runnable " + name + ", it is needed to set cron");
                        break;
                    }
                    ScheduledFuture<?> future = schedule.schedule(runnable, new CronTrigger(tip.cron()));
                    if (future != null) {
                        futureMap.put(name, future);
                    }
                    break;
                }
                case REPEAT_DELAY: {
                    if (tip.interval() == 0) {
                        PrintUtils.print(Level.WARNING, "can not load runnable " + name + ", it is needed to set interval");
                        break;
                    }
                    ScheduledFuture<?> future = schedule.scheduleWithFixedDelay(runnable, new Date(System.currentTimeMillis() + tip.unit().toMillis(tip.delay())), tip.unit().toMillis(tip.interval()));
                    futureMap.put(name, future);
                    break;
                }
                case REPEAT_RATE: {
                    if (tip.interval() == 0) {
                        PrintUtils.print(Level.WARNING, "can not load runnable " + name + ", it is needed to set interval");
                        break;
                    }
                    ScheduledFuture<?> future = schedule.scheduleAtFixedRate(runnable, new Date(System.currentTimeMillis() + tip.unit().toMillis(tip.delay())), tip.unit().toMillis(tip.interval()));
                    futureMap.put(name, future);
                    break;
                }
                case DELAY: {
                    delay(runnable, tip.unit().toMillis(tip.delay()));
                    break;
                }
                default:
                    PrintUtils.print(Level.WARNING, "no such task type " + tip.type() + " for " + name);
            }
        } else {
            PrintUtils.print(Level.WARNING, "can not insert this task: " + name);
        }
    }
}
