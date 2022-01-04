package idea.verlif.juststation.global.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/30 8:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    /**
     * 限制Key，用于区分不同的方法统计。<br/>
     * 默认为 [类名简称.方法名] 。
     */
    String key() default "";

    /**
     * 限制处理类
     */
    Class<? extends LimitHandler> handler();
}
