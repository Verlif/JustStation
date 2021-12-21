package idea.verlif.juststation.global.scheduling;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.*;
import org.springframework.stereotype.Component;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 11:46
 */
@Component
@EnableScheduling
public class SchedulingServer implements SchedulingConfigurer {

    private final List<CronTask> cronTasks;
    private final List<FixedDelayTask> delayTasks;
    private final List<FixedRateTask> rateTasks;
    private final List<TriggerTask> triggerTasks;

    public SchedulingServer(
            @Autowired ApplicationContext context,
            @Autowired ScheduleConfig config) {
        cronTasks = new ArrayList<>();
        delayTasks = new ArrayList<>();
        rateTasks = new ArrayList<>();
        triggerTasks = new ArrayList<>();

        Map<String, Runnable> map = context.getBeansOfType(Runnable.class);
        for (Runnable runnable : map.values()) {
            Class<?> cl = runnable.getClass();
            ScheduledCron cron = cl.getAnnotation(ScheduledCron.class);
            if (cron != null) {
                String name = cron.value().length() == 0 ? cl.getSimpleName() : cron.value();
                if (config.isAllowed(name)) {
                    cronTasks.add(new CronTask(runnable, cron.cron()));
                }
            }
            ScheduledFixedDelay delay = cl.getAnnotation(ScheduledFixedDelay.class);
            if (delay != null) {
                String name = delay.value().length() == 0 ? cl.getSimpleName() : delay.value();
                if (config.isAllowed(name)) {
                    delayTasks.add(new FixedDelayTask(runnable, delay.interval(), delay.delay()));
                }
            }
            ScheduledFixedRate rate = cl.getAnnotation(ScheduledFixedRate.class);
            if (rate != null) {
                String name = rate.value().length() == 0 ? cl.getSimpleName() : rate.value();
                if (config.isAllowed(name)) {
                    rateTasks.add(new FixedRateTask(runnable, rate.interval(), rate.delay()));
                }
            }
        }
    }

    @Override
    public void configureTasks(@NonNull ScheduledTaskRegistrar taskRegistrar) {
        for (CronTask cronTask : cronTasks) {
            taskRegistrar.addCronTask(cronTask);
        }
        if (cronTasks.size() > 0) {
            PrintUtils.print(Level.INFO, "cronTask loaded : " + cronTasks.size());
        }

        for (FixedDelayTask delayTask : delayTasks) {
            taskRegistrar.addFixedDelayTask(delayTask);
        }
        if (delayTasks.size() > 0) {
            PrintUtils.print(Level.INFO, "delayTasks loaded : " + delayTasks.size());
        }

        for (FixedRateTask rateTask : rateTasks) {
            taskRegistrar.addFixedRateTask(rateTask);
        }
        if (rateTasks.size() > 0) {
            PrintUtils.print(Level.INFO, "rateTasks loaded : " + rateTasks.size());
        }
    }

}
