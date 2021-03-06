package idea.verlif.juststation.global.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/19 10:32
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.api")
public class ApiConfig {

    /**
     * 是否启用API策略
     */
    private boolean enable = false;

    /**
     * 屏蔽的api
     */
    private BlockedApi[] blocked = new BlockedApi[]{};

    /**
     * 转接的api
     */
    private TransferApi[] transfer = new TransferApi[]{};
}
