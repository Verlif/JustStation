package idea.verlif.juststation.global.security.login.domain;

import idea.verlif.jackson.sensible.anno.Sensitive;
import idea.verlif.jackson.sensible.anno.Strategy;
import idea.verlif.juststation.global.validation.group.Insert;
import idea.verlif.juststation.global.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 基础用户信息
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:52
 */
public class BaseUser implements Serializable {

    /**
     * 用户登录名
     */
    @Schema(name = "用户登录名")
    @Size(min = 4, max = 24, groups = {Insert.class, Update.class})
    protected String username;

    /**
     * 用户密码
     */
    @Sensitive(strategy = Strategy.ALWAYS_NULL)
    @Schema(name = "用户密码")
    @Null(groups = Update.class)
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
