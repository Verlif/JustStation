package idea.verlif.juststation.global.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/27 16:16
 */
@Configuration
public class LogConfig {

    @Bean
    @ConditionalOnMissingBean(LogService.class)
    public LogService logService() {
        return new LogServiceAto();
    }

    private static class LogServiceAto implements LogService {

        public final Map<String, Logger> loggerMap;

        public LogServiceAto() {
            loggerMap = new HashMap<>();
        }

        @Override
        public void debug(String msg) {
            getLogger().log(Level.INFO, msg);
        }

        @Override
        public void info(String msg) {
            getLogger().log(Level.INFO, msg);
        }

        @Override
        public void warn(String msg) {
            getLogger().log(Level.WARNING, msg);
        }

        @Override
        public void error(String msg) {
            getLogger().log(Level.SEVERE, msg);
        }

        private Logger getLogger() {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            String cl = elements[3].getClassName();
            Logger logger = loggerMap.get(cl);
            if (logger == null) {
                logger = Logger.getLogger(cl);
                loggerMap.put(cl, logger);
            }
            return logger;
        }
    }
}
