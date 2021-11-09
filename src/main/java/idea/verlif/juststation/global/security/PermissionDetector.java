package idea.verlif.juststation.global.security;

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
     * @param role 角色名称
     * @return true - 拥有；false - 未拥有
     */
    default boolean hasRole(String role) {
        return true;
    }

    /**
     * 是否拥有关键词
     *
     * @param key 关键词
     * @return true - 拥有；false - 未拥有
     */
    default boolean hasKey(String key) {
        return true;
    }

    /**
     * 与{@code hasRole(String)}方法相反
     *
     * @param role 角色名称
     * @return true - 没有；false - 拥有
     * @see PermissionDetector#hasRole(String)
     */
    default boolean noRole(String role) {
        return !hasRole(role);
    }

    /**
     * 与{@code hasKey(String)}方法相反
     *
     * @param key 关键词
     * @return true - 没有；false - 拥有
     * @see PermissionDetector#hasKey(String)
     */
    default boolean noKey(String key) {
        return !hasKey(key);
    }
}
