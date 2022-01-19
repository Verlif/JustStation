package idea.verlif.juststation.global.command;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/22 17:22
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.command")
public class RemCommandConfig {

    /**
     * 是否启用远程URL指令
     */
    private boolean enable = false;

    /**
     * 允许的指令，当允许指令存在元素时，则屏蔽指令无效
     */
    private String[] allowed = new String[]{};

    /**
     * 屏蔽的指令
     */
    private String[] blocked = new String[]{};
}
