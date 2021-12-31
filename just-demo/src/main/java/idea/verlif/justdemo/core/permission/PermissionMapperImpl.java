package idea.verlif.justdemo.core.permission;

import idea.verlif.justdemo.core.base.mapper.KeyMapper;
import idea.verlif.justdemo.core.base.mapper.RoleMapper;
import idea.verlif.juststation.global.security.permission.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 获取权限
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:28
 */
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Autowired
    private KeyMapper keyMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<String> getUserRoleSet(String username) {
        return roleMapper.getUserRoleSet(username);
    }

    @Override
    public Set<String> getUserKeySet(String username) {
        return keyMapper.getUserKeySet(username);
    }
}
