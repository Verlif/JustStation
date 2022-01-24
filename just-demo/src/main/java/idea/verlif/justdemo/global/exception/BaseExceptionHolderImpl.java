package idea.verlif.justdemo.global.exception;

import idea.verlif.exceptioncapture.BaseExceptionHolder;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/24 11:32
 */
@Component
public class BaseExceptionHolderImpl implements BaseExceptionHolder {

    @Override
    public Object handler(Throwable throwable) {
        PrintUtils.print(throwable);
        return new FailResult<String>().msg(
                throwable.getMessage() == null ? MessagesUtils.message("error.default") : throwable.getMessage());
    }
}
