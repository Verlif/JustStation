package idea.verlif.juststation.core.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.core.test.biz.UserBiz;
import idea.verlif.juststation.core.test.domain.query.UserQuery;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.Perm;
import idea.verlif.juststation.global.security.token.OnlineUserQuery;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/12 11:08
 */
@Api(tags = "管理员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserBiz userBiz;

    @Autowired
    private LoginService loginService;

    @GetMapping("/user/list")
    @Perm(hasRole = "admin")
    @Operation(summary = "获取用户列表")
    public BaseResult<?> listAllUser(UserQuery query) {
        return userBiz.getList(query);
    }

    @GetMapping("/user/online")
    @Perm(hasRole = "admin")
    @Operation(summary = "获取所有在线用户列表")
    public BaseResult<IPage<LoginUser<? extends BaseUser>>> listOnlineUser(OnlineUserQuery query) {
        return loginService.getOnlineUser(query);
    }

    @PostMapping("/logout")
    @Perm(hasRole = "admin")
    @Operation(summary = "强退在线用户")
    public BaseResult<?> logoutUser(
            @RequestParam @Parameter(description = "用户名", required = true) String username,
            @RequestParam(required = false) @Parameter(description = "登录标识") String tag) {
        return loginService.logoutUser(username, LoginTag.getTag(tag));
    }
}
