package idea.verlif.juststation.core.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import idea.verlif.juststation.core.test.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 11:32
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取用户的角色集
     *
     * @param username 用户名
     * @return 角色集
     */
    Set<String> getUserRoleSet(@Param("username") String username);
}
