package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.spring.exception.ExceptionHolder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/20 11:21
 */
@Component
public class RequestJsonException implements ExceptionHolder<HttpMessageNotReadableException> {

    private static final Pattern PATTERN = Pattern.compile("\\[\"(.*)\"]");

    @Override
    public Class<? extends HttpMessageNotReadableException> register() {
        return HttpMessageNotReadableException.class;
    }

    @Override
    public BaseResult<?> handler(HttpMessageNotReadableException e) {
        if (e.getMessage() != null) {
            Matcher matcher = PATTERN.matcher(e.getMessage());
            if (matcher.find()) {
                return new FailResult<>(ResultCode.FAILURE_PARAMETER).withParam(matcher.group());
            }
        }
        return new FailResult<>(ResultCode.FAILURE_PARAMETER);
    }
}
