package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.exceptioncapture.ExceptionHolder;
import idea.verlif.juststation.global.util.MessagesUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:13
 */
@Component
public class MaxUploadSizeExceededExceptionHolder implements ExceptionHolder<MaxUploadSizeExceededException> {
    @Override
    public Class<MaxUploadSizeExceededException> register() {
        return MaxUploadSizeExceededException.class;
    }

    @Override
    public BaseResult<?> handler(MaxUploadSizeExceededException e) {
        return new FailResult<>(MessagesUtils.message("result.fail.file.large"));
    }
}
