package idea.verlif.juststation.global.file.parser;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 文件解析器，用于解析文件数据
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/23 14:07
 */
public interface FileParser4List {

    /**
     * 从文件中获取数据列表
     *
     * @param file 目标文件
     * @param cl   数据类型
     * @param <T>  数据对象泛型
     * @return 数据对象列表
     */
    <T> List<T> getObjectList(File file, Class<T> cl) throws Exception;

    /**
     * 从输入流中获取数据列表
     *
     * @param inputStream 数据输入流
     * @param cl          数据类型
     * @param <T>         数据对象泛型
     * @return 数据对象列表
     */
    <T> List<T> getObjectList(InputStream inputStream, Class<T> cl) throws Exception;

    /**
     * 存储数据列表到文件
     *
     * @param list 数据对象列表
     * @param file 目标文件
     * @param <T>  数据对象泛型
     * @param cl          数据类型
     * @return 成功的数据量
     */
    <T> int saveObjectList(List<T> list, File file, Class<T> cl) throws Exception;
}
