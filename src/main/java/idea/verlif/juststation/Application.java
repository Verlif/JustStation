package idea.verlif.juststation;

import idea.verlif.juststation.global.command.CommandManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/2 10:22
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);

        // 开启指令
        // 这里用的时SpringShell，所以不用CommandManager了
//        CommandManager commandManager = context.getBean(CommandManager.class);
//        commandManager.start();
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
