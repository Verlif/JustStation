package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.global.command.exception.CommandException;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.OutUtils;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        return new FailResult<String>().msg(
                e.getMessage() == null ? MessagesUtils.message("error.default") : e.getMessage());
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

    @ExceptionHandler(value = CommandException.class)
    public void commandException(CommandException e) {
        OutUtils.printLine(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = SizeException.class)
    public BaseResult<String> sizeLimitExceededException(SizeLimitExceededException e) {
        return new FailResult<>(MessagesUtils.message("result.fail.large_resource"));
    }

    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public BaseResult<String> fileSizeLimitExceededException(MaxUploadSizeExceededException e) {
        return new FailResult<>(MessagesUtils.message("result.fail.file.large"));
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public BaseResult<String> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new BaseResult<String>(ResultCode.FAILURE_PARAMETER_LACK).withParam(e.getParameterName());
    }
}