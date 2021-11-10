package idea.verlif.juststation.global.security;

import java.util.Set;

/**
 * 权限Mapper，用于获取角色的关键词组与角色组
 *
 * @param <T> 角色标识类型，例如ID
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:46
 */
public interface PermissionMapper<T> {

    /**
     * 通过用户唯一标识获取角色组
     *
     * @param userId 用户唯一标识
     * @return 用户所在的角色组
     */
    Set<String> getUserRoleSet(T userId);

    /**
     * 通过用户唯一标识获取关键词组
     *
     * @param userId 用户唯一标识
     * @return 用户所在的关键词组
     */
    Set<String> getUserKeySet(T userId);
}
