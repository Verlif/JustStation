package idea.verlif.juststation.global.file.parser.excel;

import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.text.ParseException;

/**
 * 表格数据处理
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/2 10:39
 */
public interface CellHandler {

    /**
     * 从单元格中获取数据
     *
     * @param cell   单元格对象
     * @param field  对应的字段
     * @param target 对应的数据对象
     */
    void readFromCell(Cell cell, Field field, Object target) throws IllegalAccessException, ParseException;

    /**
     * 向单元格中填充数据
     *
     * @param cell   单元格对象
     * @param field  对应的字段
     * @param target 对应的数据对象
     */
    void writeToCell(Cell cell, Field field, Object target) throws IllegalAccessException;
}
