package idea.verlif.juststation.global.security.permission.eh;

import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.spring.exception.ExceptionHolder;
import idea.verlif.spring.permission.exception.NoPermissionException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/25 17:16
 */
@Component
public class NoPermissionExceptionHolder implements ExceptionHolder<NoPermissionException> {

    @Override
    public Class<? extends NoPermissionException> register() {
        return NoPermissionException.class;
    }

    @Override
    public Object handler(NoPermissionException e) {
        return new FailResult<>(ResultCode.FAILURE_NOT_LOGIN);
    }
}
