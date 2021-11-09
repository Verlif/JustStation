package idea.verlif.juststation.core;

import idea.verlif.juststation.core.base.BaseResult;
import idea.verlif.juststation.global.security.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:11
 */
@Tag(name = "测试")
@RestController
@RequestMapping("/test")
public class BaseController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Operation(tags = "hello")
    @GetMapping("/hello")
    public BaseResult<String> hello() {
        return new BaseResult<String>().msg("hello");
    }

    @Operation(tags = "login")
    @GetMapping("/login")
    public BaseResult<?> login() {
        return loginService.login("awsl", "7777");
    }
}
