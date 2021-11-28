package idea.verlif.juststation.global.security.login;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.login.impl.LoginHandlerAto;
import idea.verlif.juststation.global.security.token.OnlineUserQuery;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 16:19
 */
@Service
public class LoginService {

    private final LoginHandler loginHandler;

    private final TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginService(
            @Autowired TokenService tokenService,
            @Autowired(required = false) LoginHandler loginHandler) {
        this.tokenService = tokenService;
        if (loginHandler == null) {
            this.loginHandler = new LoginHandlerAto(tokenService);
        } else {
            this.loginHandler = loginHandler;
        }
    }

    /**
     * 当前用户登出所有标识
     *
     * @return 登出结果
     */
    public BaseResult<?> logoutAll() {
        int count = tokenService.logoutAll();
        if (count > 0) {
            return new OkResult<>().msg(count);
        } else {
            return new FailResult<>();
        }
    }

    /**
     * 强退用户登录
     *
     * @param userToken 用户Token
     * @return 强退结果
     */
    public BaseResult<?> logoutUser(String userToken) {
        tokenService.logout(userToken);
        return new OkResult<>();
    }

    /**
     * 强退登录用户
     *
     * @param userName 用户名
     * @param tag      登录标识
     * @return 强退结果
     */
    public BaseResult<?> logoutUser(String userName, LoginTag tag) {
        return logoutUser(LoginUser.getToken(userName, tag == null ? "*" : tag.getTag(), "*"));
    }

    /**
     * 获取所有在线用户信息
     *
     * @param query 查询条件
     * @return 在线用户信息
     */
    public BaseResult<IPage<LoginUser<? extends BaseUser>>> getOnlineUser(OnlineUserQuery query) {
        List<LoginUser<? extends BaseUser>> set = tokenService.getOnlineUser(query);
        return new OkResult<>(PageUtils.page(set, query));
    }

    public <T extends LoginInfo> BaseResult<?> login(T loginInfo) {
        LoginHandler.LoginResult result = loginHandler.onLogin(loginInfo);
        if (result.allow) {
            // 用户验证
            Authentication authentication;
            try {
                // 该方法会去调用UserDetailsService#loadUserByUsername(String)方法，请在该方法中实现用户登录验证
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginInfo.getUsername(), loginInfo.getPassword()));
                LoginUser<? extends BaseUser> loginUser = (LoginUser<?>) authentication.getPrincipal();
                loginUser.setLoginTime(new Date());
                // 设定用户登录标志
                loginUser.setTag(loginInfo.getTag() == null ? LoginTag.LOCAL.getTag() : loginInfo.getTag().getTag());
                // 删除之前同标志的登录
                tokenService.logout(loginUser.getToken());
                // 调用方法
                loginHandler.loginAfterAuth(loginUser);
                // 记录并返回登录Token
                return new OkResult<>(tokenService.loginUser(loginUser));
            } catch (UsernameNotFoundException e) {
                return new BaseResult<>(ResultCode.FAILURE_LOGIN_MISSING);
            } catch (BadCredentialsException e) {
                return new BaseResult<>(ResultCode.FAILURE_LOGIN_FAIL);
            } catch (Exception e) {
                e.printStackTrace();
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
