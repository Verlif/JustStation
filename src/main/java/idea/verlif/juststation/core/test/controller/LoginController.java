package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.security.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:11
 */
@Tag(name = "登录与登出")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 此处的信息未加密，请修改登录方法自行校验
     *
     * @param loginInfo 用户登录信息
     * @return 登录结果
     */
    @Operation(summary = "登录")
    @PostMapping
    public BaseResult<?> login(@RequestBody LoginInfo loginInfo) {
        return loginService.login(loginInfo.username, loginInfo.password);
    }

    /**
     * 用户登出，让登录的token失效
     *
     * @return 登出结果
     */
    @Operation(summary = "登出")
    @PostMapping("/logout")
    public BaseResult<?> logout() {
        return loginService.logout();
    }

    @Data
    @Schema(name = "用户登录信息", description = "用于登录的用户名及密码")
    private static final class LoginInfo {

        @Schema(name = "用户登录名")
        private String username;

        @Schema(name = "用户登录密码")
        private String password;
    }
}
