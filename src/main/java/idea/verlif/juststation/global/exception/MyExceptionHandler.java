package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.util.MessagesUtils;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.SizeLimitExceededException;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:13
 */
@ControllerAdvice
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public BaseResult<String> exceptionHandler(Throwable e) {
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

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResult<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                sb.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append(", ");
            });
        }
        if (sb.length() >= 2) {
            return new BaseResult<>(ResultCode.FAILURE_PARAMETER).withParam(sb.substring(0, sb.length() - 2));
        } else {
            return new BaseResult<>(ResultCode.FAILURE_PARAMETER);
        }
    }

    @ResponseBody
    @ExceptionHandler(value = DuplicateKeyException.class)
    public BaseResult<?> duplicateKeyException(DuplicateKeyException e) {
        return new BaseResult<>(ResultCode.FAILURE_PARAMETER).withParam(MessagesUtils.message("error.duplicate_key"));
    }
}