package idea.verlif.juststation.global.security.login.domain;

import idea.verlif.juststation.global.base.domain.WithKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/12 9:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "用户登录信息", description = "用于登录的用户名及密码")
public class LoginInfo extends WithKey {

    @Schema(name = "用户登录名", required = true)
    private String username;

    @Schema(name = "用户登录密码", required = true)
    private String password;

    @Schema(name = "登录标识", defaultValue = "local")
    private LoginTag tag;

    @Schema(name = "记住登录", defaultValue = "false")
    private boolean rememberMe;

    public void setTag(LoginTag tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        this.tag = LoginTag.getTag(tag);
    }
}