package idea.verlif.juststation.global.security.impl;

import idea.verlif.juststation.global.security.BaseUserMapper;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:30
 */
@Service
public class BaseUserMapperImpl implements BaseUserMapper {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public BaseUser getUserByUsername(String username) {
        BaseUser user = new BaseUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("7777"));
        return user;
    }
}
