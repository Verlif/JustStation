package idea.verlif.juststation.global.security.login.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 11:52
 */
@Data
public class BaseUser implements Serializable {

    /**
     * 用户登录名
     */
    @Schema(name = "用户登录名")
    private String username;

    /**
     * 用户密码
     */
    @Schema(name = "用户密码")
    private String password;
}
