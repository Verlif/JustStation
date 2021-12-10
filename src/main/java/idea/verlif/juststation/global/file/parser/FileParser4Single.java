package idea.verlif.juststation.global.file.parser;

import java.io.File;
import java.io.InputStream;

/**
 * 文件解析器，用于解析文件数据
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/23 14:07
 */
public interface FileParser4Single {

    /**
     * 从文件中获取数据
     *
     * @param file 目标文件
     * @param <T>  数据对象泛型
     * @param cl   数据类型
     * @return 数据对象
     */
    <T> T getObject(File file, Class<T> cl);

    /**
     * 从输入流中获取数据
     *
     * @param inputStream 数据输入流
     * @param <T>         数据对象泛型
     * @param cl          数据类型
     * @return 数据对象
     */
    <T> T getObject(InputStream inputStream, Class<T> cl);

    /**
     * 存储数据到文件
     *
     * @param object 数据对象
     * @param file   目标文件
     * @param <T>    数据对象泛型
     * @return 是否成功存储
     */
    <T> boolean saveObject(T object, File file);
}
