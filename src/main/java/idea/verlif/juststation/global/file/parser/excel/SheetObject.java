package idea.verlif.juststation.global.file.parser.excel;

import java.lang.annotation.*;

/**
 * Sheet表对象
 * @author Verlif
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SheetObject {

    /**
     * Sheet名称
     * @return 数据对应的Sheet名称
     */
    String value() default "Sheet";

    /**
     * 数据起始行
     * @return 起始行数
     */
    int lineStart() default 2;
}
