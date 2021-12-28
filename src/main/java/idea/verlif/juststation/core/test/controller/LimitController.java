package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.test.handler.TestApiLogHandler;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.limit.Limit;
import idea.verlif.juststation.global.limit.impl.DefaultLimitHandler;
import idea.verlif.juststation.global.limit.impl.RandomLimitHandler;
import idea.verlif.juststation.global.log.LogIt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问限制测试
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/29 11:30
 */
@RestController
@RequestMapping("/public")
@Api(tags = "访问限制测试")
public class LimitController {

    @LogIt(message = "test", handler = TestApiLogHandler.class)
    @Limit(key = "test1", handler = DefaultLimitHandler.class)
    @GetMapping("/test1")
    public BaseResult<String> test1() {
        return new OkResult<>();
    }

    @LogIt(message = "test2")
    @Limit(key = "test2", handler = RandomLimitHandler.class)
    @GetMapping("/test2")
    public BaseResult<String> test2() {
        return new OkResult<>(test());
    }

    public String test() {
        return "test";
    }
}
