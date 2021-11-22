package idea.verlif.juststation.global.security.login.impl;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.login.LoginHandler;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 14:24
 */
public class LoginHandlerAto implements LoginHandler {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public LoginHandlerAto(
            @Autowired TokenService tokenService,
            @Autowired AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
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
