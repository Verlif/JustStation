package idea.verlif.justdemo.core.norepeat;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.spring.norepeat.NoRepeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/31 11:20
 */
@NoRepeat(interval = 10000)
@RestController
public class NoRepeatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoRepeatController.class);

    @NoRepeat(interval = 10000, message = "不要重复提交，知道吗？？？？？？？？？？")
    @GetMapping("/norepeat")
    public BaseResult<String> norepeat(String test) {
        LOGGER.info("norepeat - " + test);
        return new OkResult<>(test);
    }

    @NoRepeat(interval = 10000, isIgnored = true)
    @GetMapping("/alwaysRepeat")
    public BaseResult<String> alwaysRepeat(String test) {
        LOGGER.info("alwaysRepeat - " + test);
        return new OkResult<>(test);
    }

}
