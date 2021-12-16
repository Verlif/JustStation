package idea.verlif.juststation.global.file.parser;

import idea.verlif.juststation.global.file.FileType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/16 15:20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Parser4Single {

    /**
     * 绑定的文件类型
     */
    FileType[] fileType();
}
