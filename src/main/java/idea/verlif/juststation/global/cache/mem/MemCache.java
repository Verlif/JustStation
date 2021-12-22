package idea.verlif.juststation.global.cache.mem;

import idea.verlif.juststation.global.cache.CacheHandler;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * HashMap做的简易缓存，暂不支持持久化 </br>
 * 对于有效时间做的就是懒删除 <br/>
 * 对于Key值的模糊匹配就是用的遍历
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 10:56
 */
public class MemCache implements CacheHandler, Serializable {

    private final Map<String, Object> map;
    private final Map<String, Long> deadMap;

    public MemCache() {
        map = new ConcurrentHashMap<>();
        deadMap = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void put(String key, T value, long timeout, TimeUnit timeUnit) {
        map.put(key, value);
        if (timeout > 0 && timeUnit != null) {
            deadMap.put(key, System.currentTimeMillis() + timeUnit.toMillis(timeout));
        }
    }

    @Override
    public boolean expire(String key, long timeout, TimeUnit unit) {
        if (deadMap.containsKey(key)) {
            deadMap.put(key, System.currentTimeMillis() + unit.toMillis(timeout));
            return true;
        }
        return false;
    }

    @Override
    public <T> T get(String key) {
        Long t = deadMap.get(key);
        if (t != null && t < System.currentTimeMillis()) {
            map.remove(key);
            deadMap.remove(key);
            return null;
        }
        return (T) map.get(key);
    }

    @Override
    public boolean remove(String key) {
        map.remove(key);
        deadMap.remove(key);
        return !map.containsKey(key);
    }

    @Override
    public int removeByMatch(String match) {
        match = match.replaceAll("\\*", ".*");
        Pattern pattern = Pattern.compile(match);
        int c = 0;
        Set<String> s = new HashSet<>(map.keySet());
        for (String key : s) {
            if (pattern.matcher(key).matches()) {
                remove(key);
                c++;
            }
        }
        return c;
    }

    @Override
    public Set<String> findKeyByMatch(String match) {
        match = match.replaceAll("\\*", ".*");
        Pattern pattern = Pattern.compile(match);
        Set<String> s = new HashSet<>();
        for (String key : map.keySet()) {
            if (pattern.matcher(key).matches() && get(key) != null) {
                s.add(key);
            }
        }
        return s;
    }

    /**
     * 用作测试的方法
     */
    public static void main(String[] args) {
        int max = 500000;
        String match = "*2*";
        System.out.println("添加开始\t\t\t\t: " + System.currentTimeMillis());
        MemCache cache = new MemCache();
        for (int i = 0; i < max; i++) {
            // 过期值设定
            cache.put(String.valueOf(i), i, i, TimeUnit.MILLISECONDS);
        }
        System.out.println("添加结束\t\t\t\t: " + System.currentTimeMillis() + " - size: " + cache.findKeyByMatch("*").size());
        // Key值搜索测试
        System.out.println("搜索7923\t\t\t: " + cache.get("7923"));
        System.out.println("单Key搜索结束\t\t: " + System.currentTimeMillis());
        // Key值匹配测试
        Set<String> s = cache.findKeyByMatch(match);
        System.out.println("Key模糊匹配结束\t\t: " + System.currentTimeMillis() + " - size: " + s.size());
        // 懒删除测试
        System.out.println("当前Size:\t\t\t: " + cache.findKeyByMatch("*").size());
        System.out.println("搜索20\t\t\t\t: " + cache.get("20"));
        System.out.println("当前Size:\t\t\t: " + cache.findKeyByMatch("*").size());
        System.out.println("搜索2000\t\t\t: " + cache.get("2000"));
        System.out.println("当前Size:\t\t\t: " + cache.findKeyByMatch("*").size());
        // 手动删除测试
        System.out.println("删除数量\t\t\t\t: " + cache.removeByMatch(match));
        System.out.println("当前Size:\t\t\t: " + cache.findKeyByMatch("*").size());
    }
}
