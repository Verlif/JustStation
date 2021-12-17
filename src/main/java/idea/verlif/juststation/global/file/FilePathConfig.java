package idea.verlif.juststation.global.file;

import idea.verlif.juststation.global.file.handler.DefaultFileHandler;
import idea.verlif.juststation.global.file.handler.FileHandler;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

    /**
     * 文件系统根路径
     */
    private String main = "/upload/";

    public void setMain(String main) {
        if (!main.endsWith(DIR_SPLIT)) {
            this.main = main + DIR_SPLIT;
        } else {
            this.main = main;
        }
    }

    @Bean
    @ConditionalOnMissingBean(FileHandler.class)
    public FileHandler fileHandler() {
        return new DefaultFileHandler(this);
    }
}
