package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.core.base.BaseResult;
import idea.verlif.juststation.core.base.ext.FailResult;
import idea.verlif.juststation.core.base.ext.OkResult;
import idea.verlif.juststation.global.security.PermissionMapper;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
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

    @Autowired
    private PermissionMapper permissionMapper;

    public BaseResult<?> login(String username, String password) {
        // 用户验证
        Authentication authentication;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            LoginUser<? extends BaseUser> loginUser = (LoginUser<?>) authentication.getPrincipal();
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
}
