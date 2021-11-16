package idea.verlif.juststation.global.security.login;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.core.test.domain.query.UserQuery;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.login.impl.LoginHandlerAto;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.PageUtils;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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

    private final LoginHandler loginHandler;

    private final TokenService tokenService;

    public LoginService(
            @Autowired TokenService tokenService,
            @Autowired AuthenticationManager authenticationManager,
            @Autowired(required = false) LoginHandler loginHandler) {
        this.tokenService = tokenService;
        if (loginHandler == null) {
            this.loginHandler = new LoginHandlerAto(tokenService, authenticationManager);
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
    public BaseResult<IPage<LoginUser<? extends BaseUser>>> getOnlineUser(UserQuery query) {
        Set<String> set = tokenService.getOnlineTokenList();
        List<LoginUser<? extends BaseUser>> list = new ArrayList<>();
        for (String s : set) {
            LoginUser<? extends BaseUser> loginUser = tokenService.getUserByToken(s);
            if (loginUser != null) {
                list.add(loginUser);
            }
        }
        return new OkResult<>(PageUtils.page(list, query));
    }

    public <T extends LoginInfo> BaseResult<?> login(T t) {
        return loginHandler.login(t);
    }

    public BaseResult<?> logout() {
        return loginHandler.logout();
    }

    public BaseResult<?> logout(LoginTag tag) {
        return loginHandler.logout(tag);
    }
}
