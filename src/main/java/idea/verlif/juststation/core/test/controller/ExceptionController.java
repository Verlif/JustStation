package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.exception.ExceptionHolder;
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
    public void levelOne() throws LevelOne {
        throw new LevelOne();
    }

    @GetMapping("LeveTwo")
    public void LeveTwo() throws LeveTwo {
        throw new LeveTwo();
    }

    @GetMapping("LevelThree")
    public void LevelThree() throws LevelThree {
        throw new LevelThree();
    }

    private static class LevelOne extends Exception {

    }

    private static class LeveTwo extends LevelOne {

    }

    private static class LevelThree extends LeveTwo {

    }

    @Bean
    public ExceptionHolder<LevelOne> levelOneExceptionHolder() {
        return new ExceptionHolder<LevelOne>() {
            @Override
            public Class<? extends LevelOne> register() {
                return LevelOne.class;
            }

            @Override
            public BaseResult<?> handler(LevelOne e) {
                return new OkResult<>("levelOne");
            }
        };
    }

    @Bean
    public ExceptionHolder<LeveTwo> levelTwoExceptionHolder() {
        return new ExceptionHolder<LeveTwo>() {
            @Override
            public Class<? extends LeveTwo> register() {
                return LeveTwo.class;
            }

            @Override
            public BaseResult<?> handler(LeveTwo e) {
                return new OkResult<>("LeveTwo");
            }
        };
    }

    @Bean
    public ExceptionHolder<LevelThree> levelThreeExceptionHolder() {
        return new ExceptionHolder<LevelThree>() {
            @Override
            public Class<? extends LevelThree> register() {
                return LevelThree.class;
            }

            @Override
            public BaseResult<?> handler(LevelThree e) {
                return new OkResult<>("LevelThree");
            }
        };
    }
}
