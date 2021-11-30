package idea.verlif.juststation.global.limit.impl;

import idea.verlif.juststation.global.limit.LimitHandler;
import org.springframework.stereotype.Component;

/**
 * 随机限制，用于测试。<br/>
 * 通过{@linkplain Math#random()}方法获取随机值进行判定。
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/30 9:26
 */
@Component
public class RandomLimitHandler implements LimitHandler {

    @Override
    public boolean arrived(String key) {
        return Math.random() > 0.5;
    }
}