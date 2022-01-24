package idea.verlif.juststation.global.limit;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.spring.limit.LimitHandler;
import idea.verlif.spring.limit.NotArrivedHandler;
import idea.verlif.spring.limit.anno.Limit;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Verlif
 */
@Component
public class MyNotArrivedHandler implements NotArrivedHandler {

    @Override
    public Object noSuchHandler(Class<? extends LimitHandler> cl) {
        return new FailResult<>("No such LimitHandler - " + cl.getName());
    }

    @Override
    public Object notArrived(Method method, Limit limit) {
        return new BaseResult<>(ResultCode.FAILURE_LIMIT);
    }
}
