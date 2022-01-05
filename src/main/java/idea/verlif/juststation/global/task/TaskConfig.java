package idea.verlif.juststation.global.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/21 14:38
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.task")
public class TaskConfig {

    /**
     * 允许的定时任务列表。存在值时，屏蔽列表无效。
     */
    private List<String> allowed = new ArrayList<>();

    /**
     * 屏蔽的定时任务列表。当允许列表存在值时，屏蔽列表无效。
     */
    private List<String> blocked = new ArrayList<>();

    public boolean isAllowed(String value) {
        return allowed.size() == 0 && !blocked.contains(value) || allowed.contains(value);
    }
}
