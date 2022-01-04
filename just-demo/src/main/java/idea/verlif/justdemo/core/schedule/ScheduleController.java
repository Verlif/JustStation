package idea.verlif.justdemo.core.schedule;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.scheduling.SchedulingService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/4 11:34
 */
@Api(tags = "定时任务测试")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping
    @Operation(summary = "添加演示定时任务")
    public BaseResult<?> add() {
        String name = String.valueOf((int) (Math.random() * 100));
        schedulingService.insert(name, new DemoSchedule(name));
        return new OkResult<>(name);
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消定时任务")
    public BaseResult<?> cancel(String name) {
        if (schedulingService.cancel(name)) {
            return new OkResult<>();
        } else {
            return new FailResult<>();
        }
    }
}
