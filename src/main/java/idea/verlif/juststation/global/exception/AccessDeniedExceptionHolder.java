package idea.verlif.juststation.global.exception;

import idea.verlif.exceptioncapture.ExceptionHolder;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:05
 */
@Component
public class AccessDeniedExceptionHolder implements ExceptionHolder<AccessDeniedException> {

    @Override
    public Class<AccessDeniedException> register() {
        return AccessDeniedException.class;
    }

    @Override
    public BaseResult<?> handler(AccessDeniedException e) {
        return new BaseResult<>(ResultCode.FAILURE_UNAVAILABLE);
    }
}
