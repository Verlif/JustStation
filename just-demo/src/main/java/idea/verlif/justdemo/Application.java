package idea.verlif.justdemo;

import idea.verlif.spring.exception.EnableExceptionCapture;
import idea.verlif.spring.limit.EnableLimit;
import idea.verlif.spring.logging.EnableLogService;
import idea.verlif.spring.permission.anno.EnablePermission;
import idea.verlif.spring.taskservice.EnableTaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/2 10:22
 */
@SpringBootApplication
@EnableExceptionCapture
@EnableTaskService
@EnableLimit
@EnableLogService
@EnablePermission
@ComponentScan({"idea.verlif.juststation", "idea.verlif.justdemo"})
public class Application extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static void close() {
        context.close();
    }
}
