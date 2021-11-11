package idea.verlif.juststation.global.security;

import idea.verlif.juststation.global.file.FilePathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/14 9:41
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private final FilePathConfig pathConfig;

    public WebMvcConfig(@Autowired FilePathConfig pathConfig) {
        this.pathConfig = pathConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FilePathConfig.TAG + "**")
                .addResourceLocations("file:" + pathConfig.getMain());
        registry.addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}