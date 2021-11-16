package idea.verlif.juststation.global.util;

import idea.verlif.juststation.global.component.PrintInfoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 输出工具
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 9:09
 */
@Component
public class OutUtils {

    /**
     * 打印基础类
     */
    private static PrintInfoHandler handler;

    static {
        handler = new ConsolePrintHandler();
    }

    public OutUtils(@Autowired(required = false) PrintInfoHandler printInfoHandler) {
        if (printInfoHandler != null) {
            handler = printInfoHandler;
        }
    }

    /**
     * 单行打印信息
     *
     * @param msg 需单行打印的信息
     * @see PrintInfoHandler#printLine(String)
     */
    public static void printLine(String msg) {
        handler.printLine(msg);
    }

    /**
     * 打印log信息
     *
     * @param level log等级
     * @param msg   log信息
     * @see PrintInfoHandler#printLog(Level, String)
     */
    public static void printLog(Level level, String msg) {
        handler.printLog(level, msg);
    }

    /**
     * 控制台打印类 <br/>
     * 这里做了个Map，方便取值
     */
    private static class ConsolePrintHandler implements PrintInfoHandler {

        private final HashMap<String, Logger> loggerHashMap;

        public ConsolePrintHandler() {
            loggerHashMap = new HashMap<>();
        }

        @Override
        public void printLine(String msg) {
            System.out.println(msg);
        }

        @Override
        public void printLog(Level level, String msg) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            Logger logger;
            if (elements.length > 3) {
                logger = getOrCreateLogger(elements[3].getClassName());
                msg = elements[3].getMethodName() + " ==> " + msg;
            } else {
                logger = getOrCreateLogger(ConsolePrintHandler.class.getSimpleName());
            }
            logger.log(level, msg);
        }

        private Logger getOrCreateLogger(String loggerName) {
            Logger logger = loggerHashMap.get(loggerName);
            if (logger == null) {
                logger = Logger.getLogger(loggerName);
                loggerHashMap.put(loggerName, logger);
            }
            return logger;
        }
    }
}
