package idea.verlif.juststation.global.file.parser.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 17:05
 */
public class CellHandlerAto implements CellHandler {

    @Override
    public void readFromCell(Cell cell, Field field, Object target) throws IllegalAccessException, ParseException {
        // 按照数据类型填充数据
        switch (field.getType().getSimpleName()) {
            case "int":
            case "Integer":
                field.set(target, (int) cell.getNumericCellValue());
                break;
            case "long":
            case "Long":
                field.set(target, (long) cell.getNumericCellValue());
                break;
            case "double":
            case "Double":
                field.set(target, cell.getNumericCellValue());
                break;
            case "boolean":
            case "Boolean":
                field.set(target, cell.getBooleanCellValue());
                break;
            case "Date":
                if (cell.getCellType() == CellType.NUMERIC) {
                    field.set(target, cell.getDateCellValue());
                } else {
                    SimpleDateFormat sdf;
                    Column column = field.getAnnotation(Column.class);
                    if (column != null) {
                        sdf = new SimpleDateFormat(column.pattern());
                    } else {
                        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    }
                    field.set(target, sdf.parse(cell.getStringCellValue()));
                }
                break;
            default:
            case "String":
                field.set(target, cell.toString());
                break;
        }
    }

    @Override
    public void writeToCell(Cell cell, Field field, Object target) throws IllegalAccessException {
        switch (field.getType().getSimpleName()) {
            case "int":
            case "Integer":
                cell.setCellValue((int) field.get(target));
                break;
            case "long":
            case "Long":
                cell.setCellValue((long) field.get(target));
                break;
            case "double":
            case "Double":
                cell.setCellValue((double) field.get(target));
                break;
            case "boolean":
            case "Boolean":
                cell.setCellValue(field.getBoolean(target));
                break;
            case "Date":
                SimpleDateFormat sdf;
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    sdf = new SimpleDateFormat(column.pattern());
                } else {
                    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                }
                Date date = (Date) field.get(target);
                cell.setCellValue(sdf.format(date));
                break;
            default:
                cell.setCellValue(String.valueOf(field.get(target)));
        }
    }
}