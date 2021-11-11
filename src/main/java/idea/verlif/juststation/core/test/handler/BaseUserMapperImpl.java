package idea.verlif.juststation.core.test.handler;

import idea.verlif.juststation.core.test.mapper.UserMapper;
import idea.verlif.juststation.global.security.login.BaseUserMapper;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:27
 */
@Mapper
public class BaseUserMapperImpl implements BaseUserMapper<Object> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseUser getUserByUsername(String username) {
        return userMapper.getUserByName(username);
    }
}
