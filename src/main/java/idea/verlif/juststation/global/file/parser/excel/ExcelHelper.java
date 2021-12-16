package idea.verlif.juststation.global.file.parser.excel;

import idea.verlif.juststation.global.file.FileType;
import idea.verlif.juststation.global.file.parser.FileParser4List;
import idea.verlif.juststation.global.file.parser.Parser4List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.FileAlreadyExistsException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel简易处理器 <br/>
 * 能从一般表格（特指一个sheet表示一类数据）中获取列表数据，或将列表数据导出至表格文件。 <br/>
 * <p>
 * 表格数据映射对象可以使用{@linkplain SheetObject}注解进行配置，不添加则进行默认配置。<br/>
 * 表格中的列名映射属性值可以使用{@linkplain Column}注解进行配置，不添加则进行默认配置。<br/>
 * <p>
 * 可以自定义{@linkplain CellHandler}
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/7/16 10:16
 */
@Parser4List(fileType = FileType.EXCEL)
public class ExcelHelper implements FileParser4List {

    /**
     * 忽略已存在的excel文件
     */
    private boolean ignoreExistFile;

    /**
     * 忽略不支持类型的属性
     */
    private boolean ignoreUnknownField;

    private final CellHandler cellHandler;

    public ExcelHelper(@Autowired(required = false) CellHandler cellHandler) {
        if (cellHandler == null) {
            this.cellHandler = new CellHandlerAto();
        } else {
            this.cellHandler = cellHandler;
        }
    }

    public void setIgnoreExistFile(boolean ignoreExistFile) {
        this.ignoreExistFile = ignoreExistFile;
    }

    public void setIgnoreUnknownField(boolean ignoreUnknownField) {
        this.ignoreUnknownField = ignoreUnknownField;
    }

    /**
     * <p> 将Excel表中的标准表格数据解析为Java对象数据列表 </p>
     * <p> Excel表数据应该遵循第一行是标题，下面对应的行是数据的格式 </p>
     *
     * @param excel Excel表文件
     * @param cl    表数据映射的Java对象类
     * @param <T>   映射的Java泛型
     * @return 获得的数据组列表
     * @throws Exception 可能会出现Excel文件路径错误、表中数据错误、映射对象格式不匹配等问题
     * @see #getObjectList(InputStream, Class)
     */
    @Override
    public <T> List<T> getObjectList(File excel, Class<T> cl) throws Exception {
        // 判断文件是否存在
        if (excel.isFile() && excel.exists()) {
            FileInputStream fis = new FileInputStream(excel);
            return getObjectList(fis, cl);
        } else {
            throw new UnSupportedExcelException("找不到指定的文件");
        }
    }

    /**
     * <p> 将Excel表中的标准表格数据解析为Java对象数据列表 </p>
     * <p> Excel表数据应该遵循第一行是标题，下面对应的行是数据的格式 </p>
     *
     * @param inputStream Excel文件输入流
     * @param cl          表数据映射的Java对象类
     * @param <T>         映射的Java泛型
     * @return 获得的数据组列表
     * @throws Exception 可能会出现Excel文件路径错误、表中数据错误、映射对象格式不匹配等问题
     */
    @Override
    public <T> List<T> getObjectList(InputStream inputStream, Class<T> cl) throws Exception {
        Workbook wb = WorkbookFactory.create(inputStream);
        SheetObject sheetObject = cl.getAnnotation(SheetObject.class);
        Iterator<Sheet> iterator = wb.sheetIterator();
        List<T> list = new ArrayList<>();
        HashMap<Integer, String> fieldNameMap = new HashMap<>();
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            // 获取第一行与最后一行的行编号
            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();
            if (lastRowIndex - firstRowIndex < 1) {
                return list;
            } else {
                // 获取行标题，并与实体类形成映射
                Row row = sheet.getRow(firstRowIndex);
                // 从第二行开始检索数据
                firstRowIndex++;
                if (sheetObject != null) {
                    if (!sheetObject.value().equals(sheet.getSheetName())) {
                        continue;
                    }
                    firstRowIndex = sheetObject.lineStart();
                }
                fieldNameMap.putAll(buildFieldMap(row, cl));
            }
            if (fieldNameMap.size() > 0) {
                // 遍历行
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    // 获取当前行信息
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        T t = cl.newInstance();
                        // 获取第一格与最后一个编号
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        // 遍历列
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                try {
                                    Field field = getField(cl, fieldNameMap.get(cIndex));
                                    if (field == null) {
                                        continue;
                                    }
                                    field.setAccessible(true);
                                    Column column = field.getAnnotation(Column.class);
                                    if (column != null && column.ignored()) {
                                        continue;
                                    }
                                    cellHandler.readFromCell(cell, field, t);
                                } catch (IllegalArgumentException | NullPointerException e) {
                                    if (ignoreUnknownField) {
                                        throw e;
                                    }
                                }
                            }
                        }
                        list.add(t);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 构造属性与表格对应关系
     *
     * @param row 表头行
     * @param cl  对象类
     * @return 对应关系Map
     */
    private Map<? extends Integer, ? extends String> buildFieldMap(Row row, Class<?> cl) {
        Iterator<Cell> titleIt = row.cellIterator();
        HashMap<Integer, String> fieldIndexMap = new HashMap<>();
        HashMap<String, Field> fieldNameMap = new HashMap<>();
        // 获取类中所有属性
        List<Field> fields = new ArrayList<>();
        Class<?> anoCl = cl;
        do {
            Collections.addAll(fields, anoCl.getDeclaredFields());
            anoCl = anoCl.getSuperclass();
        } while (anoCl != null);
        for (Field field : fields) {
            field.setAccessible(true);
            fieldNameMap.put(field.getName(), field);
        }
        boolean get;
        while (titleIt.hasNext()) {
            get = false;
            Cell cell = titleIt.next();
            if (cell != null) {
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    if (column != null && cell.toString().equals(column.value())) {
                        fieldIndexMap.put(cell.getColumnIndex(), field.getName());
                        get = true;
                        break;
                    }
                }
                if (!get) {
                    Field field = fieldNameMap.get(cell.toString());
                    if (field != null) {
                        fieldIndexMap.put(cell.getColumnIndex(), field.getName());
                    }
                }
            }
        }
        return fieldIndexMap;
    }

    /**
     * 导出数据到Excel文件
     *
     * @param file Excel输出文件
     * @param data 导出的原始数据
     * @param cl   数据类型
     * @param <T>  数据类型
     * @throws Exception 当{@code force == false}时，输出文件已存在时会抛出 {@link FileAlreadyExistsException} 异常
     */
    @Override
    public <T> int saveObjectList(List<T> data, File file, Class<T> cl) throws Exception {
        if (!ignoreExistFile && file.exists()) {
            throw new FileAlreadyExistsException("文件已存在: " + file.getAbsolutePath());
        }

        // 初始化列名数据
        HashMap<Integer, String> fieldNameMap = buildFiledMap(cl);
        // 整理列名序号
        List<String> columnNameList = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();
        List<String> fieldIndexList = new ArrayList<>();
        for (Integer integer : fieldNameMap.keySet()) {
            int index = 0;
            for (int size = indexList.size(); index < size; index++) {
                if (integer < indexList.get(index)) {
                    break;
                }
            }
            indexList.add(index, integer);
            Field field = getField(cl, fieldNameMap.get(integer));
            if (field == null) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                columnNameList.add(index, fieldNameMap.get(integer));
            } else {
                columnNameList.add(index, column.value());
            }
            fieldIndexList.add(index, field.getName());
        }
        // 创建Excel对象
        Workbook wb = new HSSFWorkbook();
        SheetObject sheetObject = cl.getAnnotation(SheetObject.class);
        Sheet sheet;
        if (sheetObject != null) {
            sheet = wb.createSheet(sheetObject.value());
        } else {
            sheet = wb.createSheet(cl.getSimpleName());
        }
        // 创建列名行
        Row row = sheet.createRow(0);
        for (int i = 0, size = columnNameList.size(); i < size; i++) {
            row.createCell(i).setCellValue(columnNameList.get(i));
        }

        // 加载数据
        for (int x = 0; x < data.size(); x++) {
            Row rowNew = sheet.createRow(x + 1);
            T t = data.get(x);
            for (int i = 0, size = fieldIndexList.size(); i < size; i++) {
                Field field = getField(cl, fieldIndexList.get(i));
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                Object o = field.get(t);
                if (o != null) {
                    Cell cell = rowNew.createCell(i);
                    // 按照数据类型填充数据
                    try {
                        cellHandler.writeToCell(cell, field, t);
                    } catch (IllegalArgumentException e) {
                        if (!ignoreUnknownField) {
                            throw e;
                        }
                    }
                }
            }
        }

        // 写入文件
        try (OutputStream outputStream = new FileOutputStream(file)) {
            wb.write(outputStream);
        } finally {
            wb.close();
        }
        return data.size();
    }

    /**
     * 构造列名序号
     *
     * @param cl 对象类
     * @return 列名序号map
     */
    private HashMap<Integer, String> buildFiledMap(Class<?> cl) {
        HashMap<Integer, String> fieldNameMap = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        Class<?> anoCl = cl;
        do {
            Collections.addAll(fields, anoCl.getDeclaredFields());
            anoCl = anoCl.getSuperclass();
        } while (anoCl != null);
        for (Field field : fields) {
            int index = 0;
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (column.ignored()) {
                    continue;
                }
                index = column.index();
            }
            while (fieldNameMap.containsKey(index)) {
                index++;
            }
            fieldNameMap.put(index, field.getName());
        }
        return fieldNameMap;
    }

    private Field getField(Class<?> cl, String fieldName) {
        Class<?> anoCl = cl;
        Field field = null;
        do {
            try {
                field = anoCl.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException ignored) {
            }
            anoCl = anoCl.getSuperclass();
        } while (anoCl != null);
        return field;
    }

    /**
     * 不知道的excel类型
     */
    public static class UnSupportedExcelException extends Exception {
        public UnSupportedExcelException(String message) {
            super(message);
        }
    }

    public static final class CellHandlerAto implements CellHandler {

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
}
