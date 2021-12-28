package idea.verlif.juststation.global.log;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/27 16:12
 */
public interface LogService {

    /**
     * DEBUG
     *
     * @param msg 信息
     */
    void debug(String msg);

    /**
     * INFO
     *
     * @param msg 信息
     */
    void info(String msg);

    /**
     * WARN
     *
     * @param msg 信息
     */
    void warn(String msg);

    /**
     * ERROR
     *
     * @param msg 信息
     */
    void error(String msg);
}
