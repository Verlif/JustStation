package idea.verlif.juststation.global.sensible;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/13 14:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveSerialize.class)
public @interface Sensitive {

    /**
     * 脱敏策略
     */
    Strategy strategy();

    /**
     * 填充值
     */
    String value() default "";

    enum Strategy {

        /**
         * 总是填充null
         */
        ALWAYS_NULL,

        /**
         * 总是填充
         */
        ALWAYS_VALUE
    }
}
