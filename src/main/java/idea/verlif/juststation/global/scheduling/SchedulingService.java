package idea.verlif.juststation.global.scheduling;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 11:46
 */
@Component
public class SchedulingService implements ApplicationRunner {

    private final ConcurrentHashMap<String, Runnable> taskMap;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> futureMap;
    /**
     * 是否在添加时直接执行定时任务
     */
    private boolean ready;

    @Autowired
    private ThreadPoolTaskScheduler schedule;

    @Autowired
    private ScheduleConfig config;

    public SchedulingService(@Autowired ApplicationContext context) {
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
            PrintUtils.print(Level.INFO, "already loaded tasks with " + futureMap.size() + " - " + sb.substring(0, sb.length() - 2));
            ready = true;
        }
    }

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
     * 添加定时任务
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
     * @param runnable 任务对象
     */
    private void schedule(String defaultName, Runnable runnable) {
        Class<?> cl = runnable.getClass();
        boolean loaded = false;
        ScheduledCron cron = cl.getAnnotation(ScheduledCron.class);
        if (cron != null) {
            String name = defaultName != null ? defaultName : cron.value().length() == 0 ? cl.getSimpleName() : cron.value();
            if (futureMap.containsKey(name)) {
                PrintUtils.print(Level.WARNING, "already exist task " + name + "!!!");
                return;
            }
            if (config.isAllowed(name)) {
                ScheduledFuture<?> future = schedule.schedule(runnable, new CronTrigger(cron.cron()));
                if (future != null) {
                    futureMap.put(name, future);
                    loaded = true;
                } else {
                    PrintUtils.print(Level.WARNING, "can not insert this task: " + name);
                }
            }
        }
        ScheduledFixedDelay delay = cl.getAnnotation(ScheduledFixedDelay.class);
        if (delay != null) {
            String name = defaultName != null ? defaultName : delay.value().length() == 0 ? cl.getSimpleName() : delay.value();
            if (futureMap.containsKey(name)) {
                PrintUtils.print(Level.WARNING, "already exist task " + name + "!!!");
                return;
            }
            if (config.isAllowed(name)) {
                ScheduledFuture<?> future = schedule.scheduleWithFixedDelay(runnable, new Date(System.currentTimeMillis() + delay.unit().toMillis(delay.delay())), delay.unit().toMillis(delay.interval()));
                futureMap.put(name, future);
                loaded = true;
            }
        }
        ScheduledFixedRate rate = cl.getAnnotation(ScheduledFixedRate.class);
        if (rate != null) {
            String name = defaultName != null ? defaultName : rate.value().length() == 0 ? cl.getSimpleName() : rate.value();
            if (futureMap.containsKey(name)) {
                PrintUtils.print(Level.WARNING, "already exist task " + name + "!!!");
                return;
            }
            if (config.isAllowed(name)) {
                ScheduledFuture<?> future = schedule.scheduleAtFixedRate(runnable, new Date(System.currentTimeMillis() + rate.unit().toMillis(rate.delay())), rate.unit().toMillis(rate.interval()));
                futureMap.put(name, future);
                loaded = true;
            }
        }

        if (!loaded) {
            PrintUtils.print(Level.WARNING, "runnable" + cl.getName() + " can not been loaded!");
        }
    }
}
