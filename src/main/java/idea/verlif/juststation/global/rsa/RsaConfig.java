package idea.verlif.juststation.global.rsa;

import idea.verlif.juststation.global.cache.CacheHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 14:53
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.rsa")
public class RsaConfig {

    /**
     * RSA密钥对有效时长
     */
    private Long expire;

    @Bean
    @ConditionalOnMissingBean(RsaService.class)
    public RsaService rsaServer(@Autowired CacheHandler handler) {
        return new OnceRsaService(this, handler);
    }
}
