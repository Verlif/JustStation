package idea.verlif.juststation.global.security.permission;

import java.util.Set;

/**
 * 权限Mapper，用于获取角色的关键词组与角色组
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:46
 */
public interface PermissionMapper {

    /**
     * 通过用户名标识获取角色组
     *
     * @param username 用户名
     * @return 用户所在的角色组
     */
    Set<String> getUserRoleSet(String username);

    /**
     * 通过用户名标识获取关键词组
     *
     * @param username 用户名
     * @return 用户所在的关键词组
     */
    Set<String> getUserKeySet(String username);
}
