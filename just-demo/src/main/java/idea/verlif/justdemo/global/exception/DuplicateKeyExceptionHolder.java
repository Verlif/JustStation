package idea.verlif.justdemo.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.spring.exception.ExceptionHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:15
 */
@Component
public class DuplicateKeyExceptionHolder implements ExceptionHolder<DuplicateKeyException> {

    @Override
    public Class<DuplicateKeyException> register() {
        return DuplicateKeyException.class;
    }

    @Override
    public BaseResult<?> handler(DuplicateKeyException e) {
        return new BaseResult<>(ResultCode.FAILURE_PARAMETER).withParam(MessagesUtils.get("error.duplicate_key"));
    }
}
