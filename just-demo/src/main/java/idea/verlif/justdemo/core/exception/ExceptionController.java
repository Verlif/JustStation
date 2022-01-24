package idea.verlif.justdemo.core.exception;

import idea.verlif.exceptioncapture.ExceptionHolder;
import idea.verlif.justdemo.core.exception.exc.LevelOneException;
import idea.verlif.justdemo.core.exception.exc.LevelThreeException;
import idea.verlif.justdemo.core.exception.exc.LevelTwoException;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/15 9:16
 */
@Configuration
@RestController
@RequestMapping("/public/exception")
@Api(tags = "异常测试")
public class ExceptionController {

    @GetMapping("levelOne")
    public void levelOne() throws LevelOneException {
        throw new LevelOneException();
    }

    @GetMapping("LeveTwo")
    public void LeveTwo() throws LevelTwoException {
        throw new LevelTwoException();
    }

    @GetMapping("LevelThree")
    public void LevelThree() throws LevelThreeException {
        throw new LevelThreeException();
    }

    @GetMapping("throw")
    public void throwIt() throws Throwable {
        throw new Throwable();
    }

    @Bean
    public ExceptionHolder<LevelOneException> levelOneExceptionHolder() {
        return new ExceptionHolder<LevelOneException>() {
            @Override
            public Class<? extends LevelOneException> register() {
                return LevelOneException.class;
            }

            @Override
            public BaseResult<?> handler(LevelOneException e) {
                return new OkResult<>("levelOne");
            }
        };
    }

    @Bean
    public ExceptionHolder<LevelTwoException> levelTwoExceptionHolder() {
        return new ExceptionHolder<LevelTwoException>() {
            @Override
            public Class<? extends LevelTwoException> register() {
                return LevelTwoException.class;
            }

            @Override
            public BaseResult<?> handler(LevelTwoException e) {
                return new OkResult<>("LeveTwo");
            }
        };
    }

    @Bean
    public ExceptionHolder<LevelThreeException> levelThreeExceptionHolder() {
        return new ExceptionHolder<LevelThreeException>() {
            @Override
            public Class<? extends LevelThreeException> register() {
                return LevelThreeException.class;
            }

            @Override
            public BaseResult<?> handler(LevelThreeException e) {
                return new OkResult<>("LevelThree");
            }
        };
    }
}
