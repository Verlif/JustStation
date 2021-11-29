package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
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
    @GetMapping("/test")
    public BaseResult<String> test() {
        return new OkResult<>();
    }
}
