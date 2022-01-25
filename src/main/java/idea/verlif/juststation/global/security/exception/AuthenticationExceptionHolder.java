package idea.verlif.juststation.global.security.exception;

import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.spring.exception.ExceptionHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/7 11:18
 */
@Component
public class AuthenticationExceptionHolder implements ExceptionHolder<AuthenticationException> {

    @Override
    public Class<? extends AuthenticationException> register() {
        return AuthenticationException.class;
    }

    @Override
    public Object handler(AuthenticationException e) {
        return new FailResult<>(e.getMessage());
    }
}
