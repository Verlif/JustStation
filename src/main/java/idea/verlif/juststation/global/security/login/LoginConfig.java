package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.global.security.login.impl.LoginHandlerAto;
import idea.verlif.juststation.global.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public LoginHandler loginHandler(@Autowired TokenService tokenService) {
        return new LoginHandlerAto(tokenService);
    }

    @Bean
    @ConditionalOnMissingBean(BaseUserCollector.class)
    public BaseUserCollector baseUserCollector() {
        return username -> null;
    }
}
