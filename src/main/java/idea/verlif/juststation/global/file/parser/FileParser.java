package idea.verlif.juststation.global.file.parser;

import java.io.File;
import java.util.List;

/**
 * 文件解析器，用于解析文件数据
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/23 14:07
 */
public interface FileParser {

    /**
     * 从文件中获取数据
     *
     * @param file 目标文件
     * @param <T>  数据对象泛型
     * @return 数据对象
     */
    <T> T getObject(File file);

    /**
     * 从文件中获取数据列表
     *
     * @param file 目标文件
     * @param <T>  数据对象泛型
     * @return 数据对象列表
     */
    <T> List<T> getObjectList(File file);

    /**
     * 存储数据到文件
     *
     * @param object 数据对象
     * @param file   目标文件
     * @param <T>    数据对象泛型
     * @return 是否成功存储
     */
    <T> boolean saveObject(T object, File file);

    /**
     * 存储数据列表到文件
     *
     * @param list 数据对象列表
     * @param file 目标文件
     * @param <T>  数据对象泛型
     * @return 成功的数据量
     */
    <T> int saveObjectList(List<T> list, File file);
}
