package idea.verlif.juststation.global.limit.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.spring.exception.ExceptionHolder;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:17
 */
@Component
public class LimitExceptionHolder implements ExceptionHolder<LimitException> {

    @Override
    public Class<LimitException> register() {
        return LimitException.class;
    }

    @Override
    public BaseResult<?> handler(LimitException e) {
        return new BaseResult<>(ResultCode.FAILURE_LIMIT);
    }
}
