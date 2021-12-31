package idea.verlif.justdemo.core.schedule;

import idea.verlif.juststation.global.scheduling.ScheduledFixedRate;
import idea.verlif.juststation.global.util.PrintUtils;

import java.util.logging.Level;

/**
 * 演示用的定时任务。<br/>
 * 在application-station.yml中已被禁用。
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 15:04
 */
@ScheduledFixedRate(interval = 5000)
public class DemoSchedule implements Runnable {
    @Override
    public void run() {
        PrintUtils.print(Level.INFO, "this is DemoSchedule running log - " + System.currentTimeMillis());
    }
}
