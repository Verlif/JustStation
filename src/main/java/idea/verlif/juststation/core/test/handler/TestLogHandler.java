package idea.verlif.juststation.core.test.handler;

import idea.verlif.juststation.global.log.LogHandler;
import idea.verlif.juststation.global.log.LogIt;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/1 10:48
 */
@Component
public class TestLogHandler implements LogHandler {

    @Override
    public void onLog(Method method, LogIt logIt) {
        PrintUtils.println("正在访问 [" + method.getName() + "], " + logIt.message());
    }

    @Override
    public void onReturn(Method method, LogIt logIt, Object o) {
        PrintUtils.println("访问结束 [" + method.getName() + "], " + logIt.message());
    }
}
