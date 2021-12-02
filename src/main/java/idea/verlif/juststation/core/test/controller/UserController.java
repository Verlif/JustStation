package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.test.biz.UserBiz;
import idea.verlif.juststation.core.test.domain.User;
import idea.verlif.juststation.core.test.domain.req.UpdatePassword;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.security.permission.Perm;
import idea.verlif.juststation.global.validation.Insert;
import idea.verlif.juststation.global.validation.Update;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/11 14:32
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private UserBiz userBiz;

    /**
     * 注册新用户 <br/>
     * 这里使用Check注解来标记方法和参数 <br/>
     * Check标记方法表示需要检测的接口，标记参数表示需要检测的对象（该对象需要实现Checkable接口）
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @Operation(summary = "注册")
    @PostMapping("/register")
    public BaseResult<?> register(
            @RequestBody @Validated(Insert.class) User user
    ) {
        return userBiz.register(user);
    }

    @Perm(hasKey = "user")
    @Operation(summary = "获取个人信息")
    @GetMapping("/self")
    public BaseResult<?> selfInfo() {
        return userBiz.getSelfInfo();
    }

    @Perm(hasKey = "user")
    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public BaseResult<User> getUserInfo(@RequestParam(required = false) String username) {
        return userBiz.getInfoByName(username);
    }

    @Perm(hasKey = "user")
    @Operation(summary = "修改个人信息")
    @PutMapping
    public BaseResult<?> update(@RequestBody @Validated(Update.class) User user) {
        return userBiz.update(user);
    }

    @Perm(hasKey = "user")
    @Operation(summary = "修改个人密码")
    @PutMapping("/password")
    public BaseResult<?> updatePassword(@RequestBody UpdatePassword up) {
        return userBiz.updatePassword(up);
    }
}
