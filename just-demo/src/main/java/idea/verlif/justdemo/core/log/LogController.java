package idea.verlif.justdemo.core.log;

import idea.verlif.juststation.global.log.LogIt;
import idea.verlif.juststation.global.util.PrintUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 14:52
 */
@Api(tags = "日志测试接口")
@RestController("/log")
public class LogController {

    @RequestMapping("/demo")
    @LogIt(message = "demo", handler = DemoApiLogHandler.class)
    public void demo() {
        PrintUtils.print("demo");
    }
}
