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
@ConfigurationProperties(prefix = "station.file.path")
public class FilePathConfig {

    public static final String TAG = "/file/";
    public static final String DIR_SPLIT = "/";

    private String main = "/upload/";

    public void setMain(String main) {
        if (!main.endsWith(DIR_SPLIT)) {
            this.main = main + DIR_SPLIT;
        } else {
            this.main = main;
        }
    }
}
