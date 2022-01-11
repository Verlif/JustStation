package idea.verlif.juststation.global.file;

import idea.verlif.juststation.global.file.impl.DefaultFileService;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "station.file")
public class FileConfig {

    /**
     * 文件访问URL前缀
     */
    public static final String TAG = "/file/";
    /**
     * 文件路径分隔符
     */
    public static final String DIR_SPLIT = File.separator;

    /**
     * 路径配置
     */
    private Path path;

    public FileConfig() {
        path = new Path();
    }

    /**
     * 获取文件根路径
     *
     * @return 文件系统根路径
     */
    public String getMain() {
        return path.getMain();
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService fileService() {
        return new DefaultFileService(this);
    }

    private static final class Path {

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

        public String getMain() {
            return main;
        }
    }
}
