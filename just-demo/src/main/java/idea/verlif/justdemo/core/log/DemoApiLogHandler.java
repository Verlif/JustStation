package idea.verlif.justdemo.core.log;

import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.spring.logging.api.ApiLogHandler;
import idea.verlif.spring.logging.api.LogIt;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 演示用接口日志处理
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/1 10:48
 */
@Component
public class DemoApiLogHandler implements ApiLogHandler {

    @Override
    public void onLog(Method method, LogIt logIt, long time) {
        PrintUtils.println("正在访问 [" + method.getName() + "], " + logIt.message() + " at " + new Date(time));
    }

    @Override
    public void onReturn(Method method, LogIt logIt, Object o, long time) {
        PrintUtils.println("访问结束 [" + method.getName() + "], " + logIt.message() + " at " + new Date(time));
    }
}
