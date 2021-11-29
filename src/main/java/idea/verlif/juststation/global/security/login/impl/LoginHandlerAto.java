package idea.verlif.juststation.global.security.login.impl;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.login.LoginHandler;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 14:24
 */
public class LoginHandlerAto implements LoginHandler {

    private final TokenService tokenService;

    public LoginHandlerAto(
            @Autowired TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public <T extends LoginInfo> LoginResult onLogin(T t) {
        return LoginResult.allowed();
    }

    @Override
    public BaseResult<?> loginWithExist(Set<String> tokens) {
        // 删除之前同标志的登录
        for (String token : tokens) {
            tokenService.logout(token);
        }
        return new OkResult<>();
    }

    @Override
    public <T extends LoginUser<? extends BaseUser>> void loginAfterAuth(T loginUser) {
    }

    /**
     * 当前用户登出
     *
     * @return 登出结果
     */
    @Override
    public BaseResult<?> logout() {
        LoginUser<?> user = SecurityUtils.getLoginUser();
        if (user != null) {
            tokenService.logout(user.getToken());
            return new OkResult<>();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN);
        }
    }

}
