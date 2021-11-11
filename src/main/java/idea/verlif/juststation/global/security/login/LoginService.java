package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 登录结果
     */
    public BaseResult<?> login(String username, String password) {
        return login(username, password, "local");
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @param tag      登录标识，用于限制登录
     * @return 登录结果
     */
    public BaseResult<?> login(String username, String password, String tag) {
        // 用户验证
        Authentication authentication;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            LoginUser<? extends BaseUser> loginUser = (LoginUser<?>) authentication.getPrincipal();
            loginUser.setTag(tag);

            // 删除之前的登录
            tokenService.delLoginUser(loginUser.getToken());
            // 生成token
            return new OkResult<String>().msg("登陆成功").data(tokenService.createToken(loginUser));
        } catch (NullPointerException | UsernameNotFoundException ignored) {
            return new FailResult<>().msg("用户不存在");
        } catch (BadCredentialsException e) {
            return new FailResult<>().msg("用户密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            return new FailResult<>().msg("未知错误");
        }
    }

    /**
     * 当前用户登出
     *
     * @return 登出结果
     */
    public BaseResult<?> logout() {
        LoginUser<?> user = SecurityUtils.getLoginUser();
        if (user != null) {
            tokenService.delLoginUser(user.getToken());
            return new OkResult<>().msg("登出成功");
        } else {
            return new FailResult<>("没有登录用户");
        }
    }
}
