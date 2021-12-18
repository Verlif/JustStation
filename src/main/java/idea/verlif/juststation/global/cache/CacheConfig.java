package idea.verlif.juststation.global.cache;

import idea.verlif.juststation.global.cache.mem.MemCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 16:02
 */
@Configuration
public class CacheConfig {

    @Bean
    @ConditionalOnMissingBean(CacheHandler.class)
    public CacheHandler cacheHandler() {
        return new MemCache();
    }
}