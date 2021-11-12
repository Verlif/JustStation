package idea.verlif.juststation.global.security.impl;

import idea.verlif.juststation.global.security.login.BaseUserMapper;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author Verlif
 */
@Service
public class UserDetailsServiceImpl<T> implements UserDetailsService {

    @Autowired
    private BaseUserMapper<T> userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser user = userMapper.getUserByUsername(username);
        if (user == null) {
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
    private UserDetails createLoginUser(BaseUser user) {
        return new LoginUser<>(user).withPermission(
                permissionMapper.getUserKeySet(user.getUsername()),
                permissionMapper.getUserRoleSet(user.getUsername()));
    }
}
