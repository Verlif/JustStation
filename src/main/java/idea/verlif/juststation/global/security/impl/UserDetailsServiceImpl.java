package idea.verlif.juststation.global.security.impl;

import idea.verlif.juststation.global.security.login.BaseUserMapper;
import idea.verlif.juststation.global.security.permission.PermissionMapper;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl<T> implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private BaseUserMapper<T> userMapper;

    @Autowired
    private PermissionMapper<T> permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser<T> user = userMapper.getUserByUsername(username);
        if (user == null) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        return createLoginUser(user);
    }

    /**
     * 创建登录用户对象
     *
     * @param user 原始用户信息
     * @return 登录用户信息
     */
    private UserDetails createLoginUser(BaseUser<T> user) {
        return new LoginUser<>(user).withPermission(
                permissionMapper.getUserKeySet(user.getUserId()),
                permissionMapper.getUserRoleSet(user.getUserId()));
    }
}
