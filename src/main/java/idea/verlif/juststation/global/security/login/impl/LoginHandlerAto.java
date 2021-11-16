package idea.verlif.juststation.global.security.login.impl;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.login.LoginHandler;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

    /**
     * 用户登录
     *
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    @Override
    public BaseResult<?> login(LoginInfo loginInfo) {
        // 用户验证
        Authentication authentication;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getUsername(), loginInfo.getPassword()));
            LoginUser<? extends BaseUser> loginUser = (LoginUser<?>) authentication.getPrincipal();
            loginUser.setTag(loginInfo.getTag() == null ? LoginTag.LOCAL.getTag() : loginInfo.getTag().getTag());

            // 删除之前的登录
            tokenService.delLoginUser(loginUser.getToken());
            // 生成token
            return new OkResult<String>().data(tokenService.createToken(loginUser));
        } catch (NullPointerException | UsernameNotFoundException ignored) {
            return new BaseResult<>(ResultCode.FAILURE_LOGIN_MISSING);
        } catch (BadCredentialsException e) {
            return new BaseResult<>(ResultCode.FAILURE_LOGIN_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult<>(ResultCode.FAILURE_ERROR).withParam(e.getMessage());
        }
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
            tokenService.delLoginUser(user.getToken());
            return new OkResult<>();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN);
        }
    }

    /**
     * 当前用户登出
     *
     * @param tag 登录标识
     * @return 登出结果
     */
    @Override
    public BaseResult<?> logout(LoginTag tag) {
        LoginUser<?> user = SecurityUtils.getLoginUser();
        if (user != null) {
            user.setTag(tag.getTag());
            user.setCode("*");
            tokenService.delLoginUser(user.getToken());
            return new OkResult<>();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN);
        }
    }

}
