package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.SizeLimitExceededException;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:13
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseResult<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        return new FailResult<String>().msg(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AccessDeniedException.class)
    public BaseResult<String> accessDenied(AccessDeniedException e) {
        return new BaseResult<>(ResultCode.FAILURE_UNAVAILABLE);
    }

    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public BaseResult<String> customException(CustomException e) {
        return new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN);
    }

    @ResponseBody
    @ExceptionHandler(value = SizeException.class)
    public BaseResult<String> sizeLimitExceededException(SizeLimitExceededException e) {
        return new FailResult<>("资源过大");
    }

    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public BaseResult<String> fileSizeLimitExceededException(MaxUploadSizeExceededException e) {
        return new FailResult<>("文件过大");
    }
}