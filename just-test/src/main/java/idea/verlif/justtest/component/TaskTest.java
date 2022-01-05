package idea.verlif.justtest.component;

import idea.verlif.juststation.global.task.TaskTip;
import idea.verlif.juststation.global.task.TaskType;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/5 10:53
 */
@Component
@TaskTip(value = "test", type = TaskType.DELAY, delay = 2000)
public class TaskTest implements Runnable {

    @Override
    public void run() {
        PrintUtils.print(Level.CONFIG, "任务开始");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PrintUtils.print(Level.CONFIG, "任务结束");
    }
}
