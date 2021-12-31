package idea.verlif.justdemo.core.login;

import idea.verlif.justdemo.core.base.mapper.UserMapper;
import idea.verlif.juststation.global.security.login.BaseUserCollector;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:27
 */
@Component
public class BaseUserCollectorImpl<T> implements BaseUserCollector {

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseUser getUserByUsername(String username) {
        return userMapper.getUserByName(username);
    }
}
