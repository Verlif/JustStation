package idea.verlif.justdemo.core.base.domain.req;

import idea.verlif.juststation.global.base.domain.WithKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/17 14:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "密码修改")
public class UpdatePassword extends WithKey {

    @Schema(name = "原密码")
    private String old;

    @Schema(name = "旧密码")
    private String now;
}
