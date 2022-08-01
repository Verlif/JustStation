package idea.verlif.justdemo.core.login;

import idea.verlif.justdemo.core.base.domain.User;
import idea.verlif.justdemo.core.base.mapper.UserMapper;
import idea.verlif.juststation.global.security.login.auth.AuthHandler;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/7 10:46
 */
@Component
public class PwdAuthHandler implements AuthHandler {

    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginUser auth(String id, String token) {
        User user = userMapper.getUserByName(id);
        if (user == null) {
            throw new UsernameNotFoundException(MessagesUtils.get("result.fail.login.missing"));
        }
        if (SecurityUtils.matchesPassword(token, user.getPassword())) {
            return new LoginUser(user);
        } else {
            throw new BadCredentialsException(MessagesUtils.get("result.fail.login.pwd"));
        }
    }
}
