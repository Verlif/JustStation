package idea.verlif.juststation.global.exception.handler;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.exception.ExceptionHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:14
 */
@Component
public class MissingServletRequestParameterExceptionHolder implements ExceptionHolder<MissingServletRequestParameterException> {
    @Override
    public Class<MissingServletRequestParameterException> register() {
        return MissingServletRequestParameterException.class;
    }

    @Override
    public BaseResult<?> handler(MissingServletRequestParameterException e) {
        return new BaseResult<String>(ResultCode.FAILURE_PARAMETER_LACK).withParam(e.getParameterName());
    }
}
