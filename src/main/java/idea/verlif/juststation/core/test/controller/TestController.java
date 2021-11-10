package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.test.biz.UserBiz;
import idea.verlif.juststation.core.test.domain.User;
import idea.verlif.juststation.global.request.Check;
import idea.verlif.juststation.global.security.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:11
 */
@Tag(name = "测试")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserBiz userBiz;

    @PreAuthorize("@pd.hasKey('test')")
    @Operation(summary = "hello")
    @GetMapping("/hello")
    public BaseResult<String> hello() {
        return new BaseResult<String>().msg("hello");
    }

    @Operation(summary = "登录")
    @GetMapping("/login")
    public BaseResult<?> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        return loginService.login(username, password);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    @Check
    public BaseResult<?> register(
            @RequestBody @Check User user
    ) {
        return userBiz.register(user);
    }

    @PreAuthorize("@pd.hasKey('all')")
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public BaseResult<User> getUserInfo(@RequestParam(required = false) String username) {
        return userBiz.getUserInfo(username);
    }
}
