package idea.verlif.justtest.controller;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.limit.Limit;
import idea.verlif.juststation.global.limit.LimitHandler;
import idea.verlif.juststation.global.task.TaskService;
import idea.verlif.juststation.global.task.TaskTip;
import idea.verlif.juststation.global.task.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 11:20
 */
@RestController
public class TestController {

    private static int token = 1;

    @Autowired
    private TaskService taskService;

    @Limit(handler = TestLimitHandler.class)
    @RequestMapping("/")
    public BaseResult<String> index() {
        return new OkResult<>("Hello World!!!");
    }

    @Component
    @TaskTip(type = TaskType.REPEAT_RATE, interval = 1)
    private static class TestTask implements Runnable {

        @Override
        public void run() {
            if (token < 100) {
                token++;
            }
        }
    }

    @Component
    private static class TestLimitHandler implements LimitHandler {

        @Override
        public boolean arrived(String key) {
            if (token > 0) {
                token--;
                return true;
            } else {
                return false;
            }
        }
    }
}
