package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.spring.exception.ExceptionHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 16:15
 */
@Component
public class MethodArgumentNotValidExceptionHolder implements ExceptionHolder<MethodArgumentNotValidException> {

    @Override
    public Class<MethodArgumentNotValidException> register() {
        return MethodArgumentNotValidException.class;
    }

    @Override
    public BaseResult<?> handler(MethodArgumentNotValidException e) {
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
}
