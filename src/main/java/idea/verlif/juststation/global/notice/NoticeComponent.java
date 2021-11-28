package idea.verlif.juststation.global.notice;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 通知组件
 *
 * @author Verlif
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface NoticeComponent {

    NoticeTag[] tags();
}
