package idea.verlif.juststation.global.security.login.domain;

import idea.verlif.juststation.core.base.domain.Fillable;
import idea.verlif.juststation.global.validation.Insert;
import idea.verlif.juststation.global.validation.Update;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 基础用户信息
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:52
 */
public class BaseUser implements Serializable, Fillable {

    /**
     * 用户登录名
     */
    @Schema(name = "用户登录名")
    @Size(min = 4, max = 24, groups = {Insert.class, Update.class})
    protected String username;

    /**
     * 用户密码 <br/>
     * 这里使用了AutoFill注解，避免在传输时将密码传至客户端
     */
    @AutoFill(value = "", mode = FillMode.ALWAYS)
    @Schema(name = "用户密码")
    @Null(groups = Update.class)
    @Pattern(regexp = "[a-zA-Z0-9,.!@#]{8,24}", groups = Insert.class)
    protected String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
