package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 16:12
 */
@Configuration
public class LoginConfig {

    @Bean
    @ConditionalOnMissingBean(LoginHandler.class)
    public LoginHandler loginHandler() {
        return new LoginHandler() {
            @Override
            public LoginResult preLogin(LoginInfo t) {
                return LoginResult.denied("please define a LoginHandler to continue!");
            }

            @Override
            public BaseResult<?> authSuccess(LoginUser userDetails, String token) {
                return null;
            }

            @Override
            public BaseResult<?> logout() {
                return FailResult.empty();
            }
        };
    }

}
