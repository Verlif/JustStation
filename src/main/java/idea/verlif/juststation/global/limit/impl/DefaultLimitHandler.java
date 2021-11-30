package idea.verlif.juststation.global.limit.impl;

import idea.verlif.juststation.global.limit.LimitHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认访问限制。<br/>
 * 默认逻辑为限制每分钟的总访问次数。
 *
 * @author Verlif
 * @version 1.0/
 * @date 2021/11/30 9:20
 */
@Component
@EnableScheduling
public class DefaultLimitHandler implements LimitHandler {

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

    @Scheduled(cron = "0 0/1 * * * ?")
    private synchronized void reset() {
        countMap.replaceAll((k, v) -> COUNT_PER);
    }
}