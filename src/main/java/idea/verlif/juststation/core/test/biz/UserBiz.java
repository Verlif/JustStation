package idea.verlif.juststation.core.test.biz;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.core.test.domain.User;
import idea.verlif.juststation.core.test.mapper.UserMapper;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:20
 */
@Service
public class UserBiz {

    @Autowired
    private UserMapper baseMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取个人信息
     *
     * @return 查询结果
     */
    public BaseResult<User> getSelfInfo() {
        return getUserInfo(SecurityUtils.getUsername());
    }

    /**
     * 获取用户个人信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public BaseResult<User> getUserInfo(String username) {
        User user = baseMapper.getUserByName(username == null ? SecurityUtils.getUsername() : username);
        if (user == null) {
            return new FailResult<>("用户不存在");
        } else {
            return new OkResult<>(user);
        }
    }

    /**
     * 注册新用户
     *
     * @param user 新用户信息
     * @return 注册结果
     */
    public BaseResult<?> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            if (baseMapper.insert(user) > 0) {
                return new OkResult<>(user).msg("注册成功");
            } else {
                return new FailResult<>("注册失败");
            }
        } catch (DuplicateKeyException ignored) {
            return new FailResult<>("已存在用户名 - " + user.getUsername());
        }
    }
}
