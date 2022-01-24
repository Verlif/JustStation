package idea.verlif.justdemo.core.limit;

import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.spring.limit.LimitHandler;
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
        PrintUtils.println(key + "正在判定...");
        return Math.random() > 0.5;
    }
}
