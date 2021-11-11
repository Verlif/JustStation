package idea.verlif.juststation.core.test.handler;

import idea.verlif.juststation.core.test.mapper.KeyMapper;
import idea.verlif.juststation.core.test.mapper.RoleMapper;
import idea.verlif.juststation.global.security.permission.PermissionMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:28
 */
@Mapper
public class PermissionMapperImpl implements PermissionMapper<Integer> {

    @Autowired
    private KeyMapper keyMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<String> getUserRoleSet(Integer id) {
        return roleMapper.getUserRoleSet(id);
    }

    @Override
    public Set<String> getUserKeySet(Integer id) {
        return keyMapper.getUserKeySet(id);
    }
}
