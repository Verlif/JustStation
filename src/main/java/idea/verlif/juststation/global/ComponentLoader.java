package idea.verlif.juststation.global;

import idea.verlif.juststation.global.component.CacheHandler;
import idea.verlif.juststation.global.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 组件加载器，用于自定义组件
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 9:09
 */
@Configuration
public class ComponentLoader {

    /**
     * 缓存组件加载，默认使用Redis
     *
     * @param redisTemplate 需要的Redis控制器
     * @return 缓存控制器
     */
    @Bean
    public CacheHandler cacheHandler(@Autowired RedisTemplate<String, ?> redisTemplate) {
        return new RedisCache(redisTemplate);
    }
}
