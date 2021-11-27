package idea.verlif.juststation.global.log;

import java.util.logging.Level;

/**
 * 日志等级
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/26 14:42
 */
public enum LogLevel {

    /**
     * debug
     */
    DEBUG(0, Level.CONFIG),
    /**
     * info
     */
    INFO(1, Level.INFO),
    /**
     * warning
     */
    WARNING(2, Level.WARNING),
    /**
     * error
     */
    ERROR(3, Level.SEVERE);

    private final int value;
    private final Level level;

    LogLevel(int value, Level level) {
        this.value = value;
        this.level = level;
    }

    public int getValue() {
        return value;
    }

    public Level getLevel() {
        return level;
    }
}
