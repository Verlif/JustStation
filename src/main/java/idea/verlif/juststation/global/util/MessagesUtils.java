package idea.verlif.juststation.global.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 11:04
 */
@Component
public class MessagesUtils {

    private static final Object[] ARGS_EMPTY = new Object[]{};
    private static MessageSource ms;

    /**
     * 以注入的方式，为静态参数赋值
     *
     * @param messageSource 信息源
     */
    public MessagesUtils(@Autowired MessageSource messageSource) {
        ms = messageSource;
    }

    public static String get(String code, String... args) {
        try {
            return ms.getMessage(code, args, Locale.getDefault());
        } catch (Exception ignored) {
            return code;
        }
    }

    public static String get(String code, String defaultValue) {
        try {
            return ms.getMessage(code, ARGS_EMPTY, Locale.getDefault());
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static String get(String code, Locale locale) {
        try {
            return ms.getMessage(code, ARGS_EMPTY, locale);
        } catch (Exception ignored) {
            return code;
        }
    }

    public static String get(String code, String defaultValue, Locale locale) {
        try {
            return ms.getMessage(code, ARGS_EMPTY, locale);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static String get(String code, Locale locale, String... args) {
        try {
            return ms.getMessage(code, args, locale);
        } catch (Exception ignored) {
            return code;
        }
    }
}
