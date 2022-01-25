package idea.verlif.juststation.global.security.permission.eh;

import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.spring.exception.ExceptionHolder;
import idea.verlif.spring.permission.exception.NoPermDataException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/25 17:16
 */
@Component
public class NoPermDataExceptionHolder implements ExceptionHolder<NoPermDataException> {

    @Override
    public Class<? extends NoPermDataException> register() {
        return NoPermDataException.class;
    }

    @Override
    public Object handler(NoPermDataException e) {
        return new FailResult<>(ResultCode.FAILURE_NOT_LOGIN);
    }
}
