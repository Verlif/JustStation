package idea.verlif.justdemo.core.log;

import idea.verlif.juststation.global.log.Insert;
import idea.verlif.juststation.global.log.Update;
import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.spring.logging.LogLevel;
import idea.verlif.spring.logging.LogService;
import idea.verlif.spring.logging.api.LogIt;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 14:52
 */
@Api(tags = "日志测试接口")
@RestController("/log")
@LogIt(message = "放在controller上的接口日志注解")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/demo")
    @LogIt(message = "demo", handler = DemoApiLogHandler.class)
    public void demo() {
        PrintUtils.print("demo");
    }

    @GetMapping("/insert")
    @LogIt(message = "insert it", type = Insert.class)
    public void insert() {
        logService.debug("insert it");
    }

    @GetMapping("/update")
    @LogIt(message = "update it", type = Update.class)
    public void update() {
        logService.info("update it");
    }

    @GetMapping("/debug")
    @LogIt(message = "debug it", level = LogLevel.DEBUG)
    public void debug() {
        logService.warn("debug it");
    }

    @GetMapping("/warn")
    @LogIt(message = "warn it", level = LogLevel.WARNING)
    public void warn() {
        logService.error("warn it");
    }

    @GetMapping("/test")
    public void test() {
        logService.debug("test");
    }
}
