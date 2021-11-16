package idea.verlif.juststation.global.component;

import java.util.logging.Level;

/**
 * 信息打印接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 9:11
 */
public interface PrintInfoHandler {

    /**
     * 打印单行数据
     *
     * @param msg 需要打印的信息
     */
    void printLine(String msg);

    /**
     * 打印Log信息
     *
     * @param level log等级
     * @param msg   log信息
     */
    void printLog(Level level, String msg);
}
