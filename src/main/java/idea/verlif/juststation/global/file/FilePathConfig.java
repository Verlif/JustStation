package idea.verlif.juststation.global.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.path")
public class FilePathConfig {

    public static final String TAG = "/file";

    private String main = "/upload";

}
