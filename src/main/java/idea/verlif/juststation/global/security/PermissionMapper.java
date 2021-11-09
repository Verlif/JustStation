package idea.verlif.juststation.global.security;

import java.io.Serializable;
import java.util.HashSet;
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
     * 通过用户唯一标识获取角色组
     *
     * @param id 用户唯一标识
     * @return 用户所在的角色组
     */
    default Set<String> getUserRoleSet(Serializable id) {
        return new HashSet<>();
    }

    /**
     * 通过用户唯一标识获取关键词组
     *
     * @param id 用户唯一标识
     * @return 用户所在的关键词组
     */
    default Set<String> getUserKeySet(Serializable id) {
        return new HashSet<>();
    }
}
