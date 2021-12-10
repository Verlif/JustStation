package idea.verlif.juststation.global.file.parser.excel;

import java.lang.annotation.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/8/10 17:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Column {

    /**
     * 列名，用于导入与导出Excel时，做列名与属性的映射
     */
    String value();

    /**
     * 列名序号，用于导出Excel时，做标题排序
     */
    int index() default 0;

    /**
     * 忽略此属性，此属性不参与导入与导出
     */
    boolean ignored() default false;

    /**
     * 日期格式，仅在Date类型下生效
     */
    String pattern() default "yyyy/MM/dd HH:mm:ss";
}
