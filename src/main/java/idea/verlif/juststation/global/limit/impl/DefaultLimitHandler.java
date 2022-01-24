package idea.verlif.juststation.global.limit.impl;

import idea.verlif.juststation.global.limit.LimitHandler;
import idea.verlif.spring.taskservice.TaskTip;
import idea.verlif.spring.taskservice.TaskType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认访问限制。<br/>
 * 默认逻辑为限制每分钟的总访问次数。<br/>
 * 默认使用内存作为缓存。
 *
 * @author Verlif
 * @version 1.0/
 * @date 2021/11/30 9:20
 */
@Component
@TaskTip(type = TaskType.REPEAT_DELAY, interval = 60000)
public class DefaultLimitHandler implements LimitHandler, Runnable {

    /**
     * 单位时间内的可访问次数
     */
    private static final Integer COUNT_PER = 10;

    private final Map<String, Integer> countMap;

    public DefaultLimitHandler() {
        countMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean arrived(String key) {
        int count = getCount(key);
        if (count > 0) {
            consume(key);
            return true;
        }
        return false;
    }

    private int getCount(String key) {
        Integer count = countMap.get(key);
        return count == null ? COUNT_PER : count;
    }

    private void consume(String key) {
        countMap.put(key, getCount(key) - 1);
    }

    @Override
    public void run() {
        countMap.replaceAll((k, v) -> COUNT_PER);
    }
}
