package idea.verlif.justdemo.core.login.handler;

import idea.verlif.justdemo.global.OnlineQueryDemo;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.rsa.RsaService;
import idea.verlif.juststation.global.security.login.LoginHandler;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * 登录演示处理。<br/>
 * 这里使用了登录标志，用于标识登录的设备或方式。
 * 相同的方式登录会使得之前同方式的登录失效，也就是让之前的登录掉线。<br/>
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 14:24
 */
@Component
public class LoginHandlerDemo implements LoginHandler {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RsaService rsaService;

    @Override
    public LoginResult preLogin(LoginInfo t) {
        // 在这里对密码进行解密
        t.setPassword(rsaService.decryptByPrivateKey(t.getKeyId(), t.getPassword()));
        return LoginResult.allowed();
    }

    @Override
    public BaseResult<?> authSuccess(LoginUser loginUser) {
        loginUser.setLoginTime(new Date());
        // 设定用户登录标志
        loginUser.setTag(loginUser.getTag() == null ? LoginTag.LOCAL.getTag() : loginUser.getTag());
        // 重复登录检测
        OnlineQueryDemo query = new OnlineQueryDemo();
        query.setUsername(loginUser.getUsername());
        query.setTag(LoginTag.getTag(loginUser.getTag()));
        Set<String> tokens = tokenService.getLoginKeyList(query);
        // 失效同标志的登录token
        if (tokens != null && tokens.size() > 0) {
            // 删除之前同标志的登录
            for (String token : tokens) {
                tokenService.logout(token);
            }
        }
        // 返回登录Token
        return new OkResult<>(tokenService.loginUser(loginUser));
    }

    /**
     * 当前用户登出
     *
     * @return 登出结果
     */
    @Override
    public BaseResult<?> logout() {
        LoginUser user = SecurityUtils.getLoginUser();
        if (user != null) {
            tokenService.logout(user.getToken());
            return OkResult.empty();
        } else {
            return new BaseResult<>(ResultCode.FAILURE_NOT_LOGIN);
        }
    }

}
