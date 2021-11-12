package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.core.test.domain.query.UserQuery;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * @param loginInfo 登录信息
     * @return 登录结果
     */
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

    /**
     * 当前用户登出
     *
     * @param tag 登录标识
     * @return 登出结果
     */
    public BaseResult<?> logout(LoginTag tag) {
        LoginUser<?> user = SecurityUtils.getLoginUser();
        if (user != null) {
            user.setTag(tag.getTag());
            user.setCode("*");
            tokenService.delLoginUser(user.getToken());
            return new OkResult<>().msg("登出成功");
        } else {
            return new FailResult<>("没有登录用户");
        }
    }

    /**
     * 当前用户登出所有标识
     *
     * @return 登出结果
     */
    public BaseResult<?> logoutAll() {
        LoginUser<?> user = SecurityUtils.getLoginUser();
        user.setTag("*");
        user.setCode("*");
        tokenService.delLoginUser(user.getToken());
        return new OkResult<>();
    }

    /**
     * 强退用户登录
     *
     * @param userToken 用户Token
     * @return 强退结果
     */
    public BaseResult<?> logoutUser(String userToken) {
        tokenService.delLoginUser(userToken);
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
    public BaseResult<List<LoginUser<? extends BaseUser>>> getOnlineUser(UserQuery query) {
        Set<String> set = tokenService.getOnlineTokenList();
        List<LoginUser<? extends BaseUser>> list = new ArrayList<>();
        for (String s : set) {
            LoginUser<? extends BaseUser> loginUser = tokenService.getUserByToken(s);
            if (loginUser != null) {
                list.add(loginUser);
            }
        }
        return new OkResult<>(list);
    }
}
