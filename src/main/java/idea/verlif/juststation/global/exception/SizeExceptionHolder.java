package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.spring.exception.ExceptionHolder;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:11
 */
@Component
public class SizeExceptionHolder implements ExceptionHolder<SizeException> {
    @Override
    public Class<SizeException> register() {
        return SizeException.class;
    }

    @Override
    public BaseResult<?> handler(SizeException e) {
        return new FailResult<>(MessagesUtils.message("result.fail.large_resource"));
    }
}
