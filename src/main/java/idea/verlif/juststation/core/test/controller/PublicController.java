package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.limit.Limit;
import idea.verlif.juststation.global.limit.impl.DefaultLimitHandler;
import idea.verlif.juststation.global.limit.impl.RandomLimitHandler;
import idea.verlif.juststation.global.log.LogIt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/29 11:30
 */
@RestController
@RequestMapping("/public")
public class PublicController {

    @LogIt(message = "test")
    @Limit(key = "test1", type = DefaultLimitHandler.class)
    @GetMapping("/test1")
    public BaseResult<String> test1() {
        return new OkResult<>();
    }

    @LogIt(message = "test2")
    @Limit(key = "test2", type = RandomLimitHandler.class)
    @GetMapping("/test2")
    public BaseResult<String> test2() {
        return new OkResult<>(test());
    }

    public String test() {
        return "test";
    }
}
