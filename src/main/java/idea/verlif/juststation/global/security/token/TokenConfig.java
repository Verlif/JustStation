package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.security.token.impl.DefaultTokenService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Token配置
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:58
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.token")
public class TokenConfig {

    /**
     * token存储名称
     */
    public static final String TOKEN_NAME = "station:token";

    /**
     * Token在请求中header的属性名
     */
    private String header = "Authorization";

    /**
     * TokenKey命名头
     */
    private String domain = "station:token:";

    /**
     * Token密钥
     */
    private String secret = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Token存活时间（单位：毫秒）
     */
    private Long expireTime = 3600000L;

    /**
     * 记住登录时间（单位：毫秒）
     */
    private Long remember = 14515200000L;

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime * 60000;
    }

    public void setRemember(Long remember) {
        this.remember = remember * 86400000;
    }

    @Bean
    @ConditionalOnMissingBean(TokenService.class)
    public TokenService tokenService(
            @Autowired CacheHandler cacheHandler) {
        return new DefaultTokenService(cacheHandler, this);
    }
}
