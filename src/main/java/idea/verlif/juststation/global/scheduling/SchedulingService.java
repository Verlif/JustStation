package idea.verlif.juststation.global.scheduling;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 11:46
 */
@Component
@EnableScheduling
public class SchedulingService implements SchedulingConfigurer {

    private final Map<String, CronTask> cronTasks;
    private final Map<String, FixedDelayTask> delayTasks;
    private final Map<String, FixedRateTask> rateTasks;

    public SchedulingService(
            @Autowired ApplicationContext context,
            @Autowired ScheduleConfig config) {
        cronTasks = new ConcurrentHashMap<>();
        delayTasks = new ConcurrentHashMap<>();
        rateTasks = new ConcurrentHashMap<>();

        Map<String, Runnable> map = context.getBeansOfType(Runnable.class);
        for (Runnable runnable : map.values()) {
            Class<?> cl = runnable.getClass();
            ScheduledCron cron = cl.getAnnotation(ScheduledCron.class);
            if (cron != null) {
                String name = cron.value().length() == 0 ? cl.getSimpleName() : cron.value();
                if (config.isAllowed(name)) {
                    cronTasks.put(name, new CronTask(runnable, cron.cron()));
                }
            }
            ScheduledFixedDelay delay = cl.getAnnotation(ScheduledFixedDelay.class);
            if (delay != null) {
                String name = delay.value().length() == 0 ? cl.getSimpleName() : delay.value();
                if (config.isAllowed(name)) {
                    delayTasks.put(name, new FixedDelayTask(
                            runnable,
                            delay.unit().toMillis(delay.interval()),
                            delay.unit().toMillis(delay.delay())));
                }
            }
            ScheduledFixedRate rate = cl.getAnnotation(ScheduledFixedRate.class);
            if (rate != null) {
                String name = rate.value().length() == 0 ? cl.getSimpleName() : rate.value();
                if (config.isAllowed(name)) {
                    rateTasks.put(name, new FixedRateTask(
                            runnable,
                            rate.unit().toMillis(rate.interval()),
                            rate.unit().toMillis(rate.delay())));
                }
            }
        }
    }

    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
        for (CronTask cronTask : cronTasks.values()) {
            taskRegistrar.addCronTask(cronTask);
        }
        if (cronTasks.size() > 0) {
            PrintUtils.print(Level.INFO, "cronTask loaded : " + cronTasks.size() + " - " + Arrays.toString(cronTasks.keySet().toArray()));
        }

        for (FixedDelayTask delayTask : delayTasks.values()) {
            taskRegistrar.addFixedDelayTask(delayTask);
        }
        if (delayTasks.size() > 0) {
            PrintUtils.print(Level.INFO, "delayTasks loaded : " + delayTasks.size() + " - " + Arrays.toString(delayTasks.keySet().toArray()));
        }

        for (FixedRateTask rateTask : rateTasks.values()) {
            taskRegistrar.addFixedRateTask(rateTask);
        }
        if (rateTasks.size() > 0) {
            PrintUtils.print(Level.INFO, "rateTasks loaded : " + rateTasks.size() + " - " + Arrays.toString(rateTasks.keySet().toArray()));
        }
    }

}
