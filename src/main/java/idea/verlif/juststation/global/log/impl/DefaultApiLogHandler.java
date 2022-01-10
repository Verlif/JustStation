package idea.verlif.juststation.global.log.impl;

import idea.verlif.juststation.global.log.ApiLogHandler;
import idea.verlif.juststation.global.log.LogIt;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/10 14:21
 */
@Component
public class DefaultApiLogHandler implements ApiLogHandler {

    @Override
    public void onLog(Method method, LogIt logIt) {
        PrintUtils.print(logIt.level().getLevel(), method.getName() + " >> " + logIt.message());
    }

    @Override
    public void onReturn(Method method, LogIt logIt, Object o) {
        PrintUtils.print(logIt.level().getLevel(), method.getName() + " return >> " + o);
    }

}