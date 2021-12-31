package idea.verlif.justdemo.core.controller;

import idea.verlif.justdemo.core.base.biz.UserBiz;
import idea.verlif.justdemo.core.base.domain.User;
import idea.verlif.justdemo.core.base.domain.req.UpdatePassword;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.security.permission.Perm;
import idea.verlif.juststation.global.validation.group.Update;
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
