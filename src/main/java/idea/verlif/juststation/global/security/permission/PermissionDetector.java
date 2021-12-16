package idea.verlif.juststation.global.security.permission;

import idea.verlif.juststation.global.security.login.domain.LoginUser;

/**
 * 权限判定器，对接口进行权限检测
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:28
 */
public interface PermissionDetector {

    /**
     * 是否拥有角色
     *
     * @param user 判定用户
     * @param role 角色名称
     * @return true - 拥有；false - 未拥有
     */
    boolean hasRole(LoginUser user, String role);

    /**
     * 是否拥有关键词
     *
     * @param user 判定用户
     * @param key  关键词
     * @return true - 拥有；false - 未拥有
     */
    boolean hasKey(LoginUser user, String key);

    /**
     * 与{@code hasRole(String)}方法相反
     *
     * @param user 判定用户
     * @param role 角色名称
     * @return true - 没有；false - 拥有
     * @see #hasRole(LoginUser, String)
     */
    default boolean noRole(LoginUser user, String role) {
        return !hasRole(user, role);
    }

    /**
     * 与{@code hasKey(String)}方法相反
     *
     * @param user 判定用户
     * @param key  关键词
     * @return true - 没有；false - 拥有
     * @see #hasKey(LoginUser, String)
     */
    default boolean noKey(LoginUser user, String key) {
        return !hasKey(user, key);
    }
}
