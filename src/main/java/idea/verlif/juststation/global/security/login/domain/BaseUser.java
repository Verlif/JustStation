package idea.verlif.juststation.global.security.login.domain;

import idea.verlif.juststation.core.base.domain.Checkable;
import idea.verlif.juststation.core.base.domain.Fillable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 基础用户信息
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:52
 */
public class BaseUser implements Serializable, Fillable, Checkable {

    /**
     * 用户登录名
     */
    @Schema(name = "用户登录名")
    protected String username;

    /**
     * 用户密码 <br/>
     * 这里使用了AutoFill注解，避免在传输时将密码传至客户端
     */
    @AutoFill(value = "", mode = FillMode.ALWAYS)
    @Schema(name = "用户密码")
    protected String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
