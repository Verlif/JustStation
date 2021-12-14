package idea.verlif.juststation.global.security.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.security.exception.CustomException;
import idea.verlif.juststation.global.exception.ExceptionHolder;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:10
 */
@Component
public class CustomExceptionHolder implements ExceptionHolder<CustomException> {

    @Override
    public Class<CustomException> register() {
        return CustomException.class;
    }

    @Override
    public BaseResult<?> handler(CustomException e) {
        return new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN);
    }
}
