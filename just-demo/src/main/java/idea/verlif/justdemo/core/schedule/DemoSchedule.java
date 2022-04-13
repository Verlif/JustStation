package idea.verlif.justdemo.core.schedule;

import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.spring.taskservice.anno.TaskTip;
import idea.verlif.spring.taskservice.TaskType;

import java.util.logging.Level;

/**
 * 演示用的定时任务。<br/>
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 15:04
 */
@TaskTip(type = TaskType.REPEAT_RATE, interval = 5000, auto = false)
public class DemoSchedule implements Runnable {

    private final String name;

    public DemoSchedule() {
        this.name = "DemoSchedule";
    }

    public DemoSchedule(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        PrintUtils.print(Level.INFO, "DemoSchedule " + name + " log");
    }
}
