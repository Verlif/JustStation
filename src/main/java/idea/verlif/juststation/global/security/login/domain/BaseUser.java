package idea.verlif.juststation.global.security.login.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 基础用户信息
 *
 * @param <T> 用户ID类型
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:52
 */
public class BaseUser<T> implements Serializable {

    @Schema(name = "用户ID")
    protected T userId;

    /**
     * 用户登录名
     */
    @Schema(name = "用户登录名")
    protected String username;

    /**
     * 用户密码
     */
    @Schema(name = "用户密码")
    protected String password;

    public T getUserId() {
        return userId;
    }

    public void setUserId(T userId) {
        this.userId = userId;
    }

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
