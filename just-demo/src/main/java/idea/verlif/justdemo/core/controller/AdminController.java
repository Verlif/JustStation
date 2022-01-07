package idea.verlif.justdemo.core.controller;

import idea.verlif.justdemo.core.base.biz.UserBiz;
import idea.verlif.justdemo.core.base.domain.query.UserQuery;
import idea.verlif.justdemo.core.login.service.OnlineService;
import idea.verlif.justdemo.global.OnlineQueryDemo;
import idea.verlif.juststation.global.base.domain.SimPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.log.LogIt;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.Perm;
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
@Perm(hasRole = "admin")
public class AdminController {

    @Autowired
    private UserBiz userBiz;

    @Autowired
    private LoginService loginService;

    @Autowired
    private OnlineService onlineService;

    @LogIt(message = "获取用户列表")
    @GetMapping("/user/list")
    @Operation(summary = "获取用户列表")
    public BaseResult<?> listAllUser(UserQuery query) {
        return userBiz.getPage(query);
    }

    @GetMapping("/user/online")
    @Operation(summary = "获取所有在线用户列表")
    public BaseResult<SimPage<LoginUser>> listOnlineUser(OnlineQueryDemo query) {
        return onlineService.getOnlineUser(query);
    }

    @PostMapping("/logout")
    @Operation(summary = "强退在线用户")
    public BaseResult<?> logoutUser(
            @RequestParam @Parameter(description = "用户名", required = true) String username,
            @RequestParam(required = false) @Parameter(description = "登录标识") String tag) {
        return onlineService.logoutUser(username, LoginTag.getTag(tag));
    }
}
