package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.core.base.domain.Pageable;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/22 10:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "在线用户信息查询条件")
public class OnlineUserQuery extends Pageable<BaseUser> {

    @Schema(name = "用户唯一键值")
    private String userKey;

    @Schema(name = "登录标志")
    private LoginTag loginTag;
}
