package idea.verlif.juststation.core.test.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 14:11
 */
@Data
@Schema(name = "密码修改")
public class UpdatePassword {

    @Schema(name = "原密码")
    private String old;

    @Schema(name = "旧密码")
    private String now;
}
