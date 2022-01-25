package idea.verlif.justdemo.core.limit;

import idea.verlif.justdemo.core.log.DemoApiLogHandler;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.limit.DefaultLimitHandler;
import idea.verlif.spring.limit.anno.Limit;
import idea.verlif.spring.logging.api.LogIt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问限制测试接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/29 11:30
 */
@RestController
@RequestMapping("/public")
@Api(tags = "访问限制测试")
public class LimitController {

    /**
     * 固定窗口访问限制
     */
    @LogIt(message = "defaultLimit", handler = DemoApiLogHandler.class)
    @Limit(handler = DefaultLimitHandler.class)
    @GetMapping("/defaultLimit")
    public BaseResult<String> test1() {
        return new OkResult<>(test());
    }

    /**
     * 随机访问限制
     */
    @LogIt(message = "randomLimit")
    @Limit(handler = RandomLimitHandler.class)
    @GetMapping("/randomLimit")
    public BaseResult<String> test2() {
        return new OkResult<>(test());
    }

    public String test() {
        return "test";
    }
}
