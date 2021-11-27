package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.log.LogIt;
import idea.verlif.juststation.global.log.LogType;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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
@RestController
@RequestMapping("/login")
@Api(tags = "登录与登出")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 此处的信息未加密，请修改登录方法自行校验
     *
     * @param loginInfo 用户登录信息
     * @return 登录结果
     */
    @LogIt(type = LogType.LOGIN, message = "有人登录啦啦啦")
    @Operation(summary = "登录")
    @PostMapping
    public BaseResult<?> login(@RequestBody LoginInfo loginInfo) {
        return loginService.login(loginInfo);
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
}
