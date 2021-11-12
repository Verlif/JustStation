package idea.verlif.juststation.global.component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存处理器
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 8:59
 */
public interface CacheHandler {

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit);

    /**
     * 设置有效时间
     *
     * @param key     缓存键值
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    boolean expire(final String key, final long timeout, final TimeUnit unit);

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    <T> T getCacheObject(final String key);

    /**
     * 删除单个对象
     *
     * @param key 缓存Key值
     */
    boolean deleteCacheObject(final String key);

    /**
     * 删除匹配的Key值，行为由用户自定义
     *
     * @param match 匹配词
     * @return 删除的数量
     */
    int deleteCacheByMatch(final String match);

    /**
     * 获取匹配的所有Key值
     *
     * @param match 匹配词
     * @return Key值集合
     */
    Set<String> findKeyByMatch(final String match);

    /**
     * 获得匹配的对象集合
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据集合
     */
    <T> List<T> getCacheList(final String key);
}
