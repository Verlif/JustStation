package idea.verlif.juststation.global.security;

import idea.verlif.juststation.global.security.login.domain.BaseUser;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:26
 */
public interface BaseUserMapper {

    /**
     * 获取基础用户信息
     *
     * @param username 用户登录名
     * @return 用户信息；null - 不存在该用户
     */
    default BaseUser getUserByUsername(String username) {
        return null;
    }
}
