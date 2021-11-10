package idea.verlif.juststation.global.security.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:58
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "token")
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
     * Token密钥
     */
    private String secret = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Token存活时间（单位：毫秒）
     */
    private Long expireTime = 30L;

    public TokenConfig setExpireTime(Long expireTime) {
        this.expireTime = expireTime * 60000;
        return this;
    }
}
