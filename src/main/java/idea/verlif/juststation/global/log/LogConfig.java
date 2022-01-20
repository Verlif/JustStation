package idea.verlif.juststation.global.log;

import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/27 16:16
 */
@Configuration
@ConfigurationProperties(prefix = "station.log")
public class LogConfig {

    /**
     * 日志开启等级，使用英文,隔开
     */
    private String level;

    /**
     * 日志启用类型，使用英文,隔开
     */
    private String type;

    private final List<LogLevel> allowedLevel;
    private final List<LogType> allowedType;

    public LogConfig() {
        allowedLevel = new ArrayList<>();
        allowedType = new ArrayList<>();
    }

    public void setLevel(String level) {
        this.level = level;
        for (String s : level.split(",")) {
            try {
                LogLevel l = LogLevel.valueOf(s.trim().toUpperCase(Locale.ROOT));
                allowedLevel.add(l);
            } catch (Exception ignored) {
                PrintUtils.print(Level.WARNING, "No such LogLevel at properties - " + s);
            }
        }
    }

    public String getLevel() {
        return level;
    }

    /**
     * 是否允许此等级的日志进行记录
     *
     * @param level 目标等级
     * @return 是否允许
     */
    public boolean levelable(LogLevel level) {
        return this.level == null || allowedLevel.contains(level);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        for (String s : type.split(",")) {
            try {
                LogType t = LogType.valueOf(s.trim().toUpperCase(Locale.ROOT));
                allowedType.add(t);
            } catch (Exception ignored) {
                PrintUtils.print(Level.WARNING, "No such LogType at properties - " + s);
            }
        }
    }

    /**
     * 是否允许此类型的日志进行记录
     *
     * @param type 目标类型
     * @return 是否允许
     */
    public boolean typeable(LogType type) {
        return this.type == null || allowedType.contains(type);
    }

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
                logger.setLevel(Level.CONFIG);
                loggerMap.put(cl, logger);
            }
            return logger;
        }
    }
}
