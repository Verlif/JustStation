package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.login.auth.StationAuthentication;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.PermissionMapper;
import idea.verlif.juststation.global.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 16:19
 */
@Service
public class LoginService {

    @Autowired
    private LoginHandler loginHandler;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginService() {
    }

    /**
     * 强退用户登录
     *
     * @param userToken 用户Token
     * @return 强退结果
     */
    public BaseResult<?> logoutUser(String userToken) {
        tokenService.logout(userToken);
        return OkResult.empty();
    }

    public BaseResult<?> login(LoginInfo loginInfo) {
        LoginHandler.LoginResult result = loginHandler.preLogin(loginInfo);
        if (result.allow) {
            try {
                // 验证登录信息
                Authentication authentication = authenticationManager.authenticate(new StationAuthentication(loginInfo));
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                // 填充用户权限
                loginUser.withPermission(
                        permissionMapper.getUserKeySet(loginUser.getUsername()),
                        permissionMapper.getUserRoleSet(loginUser.getUsername()));
                // 生成用户token
                String token = tokenService.loginUser(loginUser);
                // 处理认证后的对象数据
                BaseResult<?> res = loginHandler.authSuccess(loginUser, token);
                return res == null ? OkResult.empty() : res;
            } catch (UsernameNotFoundException e) {
                return new BaseResult<>(ResultCode.FAILURE_LOGIN_MISSING);
            } catch (BadCredentialsException e) {
                return new BaseResult<>(ResultCode.FAILURE_LOGIN_FAIL);
            } catch (Exception e) {
                return new BaseResult<>(ResultCode.FAILURE_ERROR).withParam(e.getMessage());
            }
        } else {
            return new FailResult<>(result.deniedMessage);
        }
    }

    public BaseResult<?> logout() {
        return loginHandler.logout();
    }

}
