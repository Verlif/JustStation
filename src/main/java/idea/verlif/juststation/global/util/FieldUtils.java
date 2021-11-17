package idea.verlif.juststation.global.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 11:08
 */
public class FieldUtils {

    /**
     * 获取类（包括父类）中的变量
     *
     * @param cl        目标类
     * @param fieldName 变量名
     * @return 变量对象；null - 未找到
     */
    public static Field getField(Class<?> cl, String fieldName) {
        if (cl == null) {
            return null;
        }
        Field thisField = null;
        do {
            try {
                thisField = cl.getDeclaredField(fieldName);
                break;
            } catch (Exception ignored) {
                cl = cl.getSuperclass();
                if (cl == null) {
                    break;
                }
            }
        } while (true);
        return thisField;
    }

    /**
     * 获取类的所有变量（包括父类）
     *
     * @param cl 目标类
     * @return 变量列表
     */
    public static List<Field> getAllField(Class<?> cl) {
        List<Field> list = new ArrayList<>();
        if (cl == null) {
            return list;
        }
        do {
            Collections.addAll(list, cl.getDeclaredFields());
            cl = cl.getSuperclass();
        } while (cl != null);
        return list;
    }
}
