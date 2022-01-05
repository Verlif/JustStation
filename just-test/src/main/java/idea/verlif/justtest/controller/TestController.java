package idea.verlif.justtest.controller;

import idea.verlif.juststation.global.task.TaskService;
import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.justtest.component.TaskTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 11:20
 */
@RestController
public class TestController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/")
    public String index() {
        taskService.delay(new TaskTest(), 2000);
        PrintUtils.print(Level.INFO, "Hello World!!!");
        return "Hello World!!!";
    }
}
