package idea.verlif.juststation.global.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知对象
 *
 * @author Verlif
 */
@Data
public class Notice {

    /**
     * 标题
     */
    @Schema(name = "标题")
    private String title;

    /**
     * 内容
     */
    @Schema(name = "内容")
    private String content;
}
