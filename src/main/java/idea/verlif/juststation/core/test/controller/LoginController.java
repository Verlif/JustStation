package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.test.biz.UserBiz;
import idea.verlif.juststation.core.test.domain.User;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.log.LogIt;
import idea.verlif.juststation.global.log.LogType;
import idea.verlif.juststation.global.rsa.RsaKey;
import idea.verlif.juststation.global.rsa.RsaServer;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.permission.Perm;
import idea.verlif.juststation.global.util.PrintUtils;
import idea.verlif.juststation.global.validation.Insert;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:11
 */
@RestController
@RequestMapping("/public")
@Api(tags = "登录与登出")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RsaServer rsaServer;

    /**
     * 由于此接口开放，所以请使用Limit注解进行访问限制，避免导致生成的僵尸密钥过多
     *
     * @return 密钥Key
     */
    @GetMapping("/puk")
    @Operation(summary = "获取公钥")
    public BaseResult<RsaKey> getPublicKey() {
        return new OkResult<>(rsaServer.genRsaKey());
    }

    /**
     * 此处的信息未加密，请修改登录方法自行校验
     *
     * @param loginInfo 用户登录信息
     * @return 登录结果
     */
    @LogIt(type = LogType.LOGIN, message = "有人登录啦啦啦")
    @PostMapping("/login")
    @Operation(summary = "登录")
    public BaseResult<?> login(@RequestBody LoginInfo loginInfo) {
        return loginService.login(loginInfo);
    }

    /**
     * 将获取的密钥Key直接填入LoginInfoWithKey对象的Key中，包括id和publicKey
     *
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    @PostMapping("/login/sim")
    @Operation(summary = "模拟登录")
    public BaseResult<?> simulatingLogin(@RequestBody LoginInfoWithKey loginInfo) {
        loginInfo.setPassword(rsaServer.encryptByPublicKey(loginInfo.getKey().getPublicKey(), loginInfo.getPassword()));
        return loginService.login(loginInfo);
    }

    /**
     * 用户登出，让登录的token失效
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "登出")
    @Perm(hasRole = "user")
    public BaseResult<?> logout() {
        return loginService.logout();
    }

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

    /**
     * 将获取的密钥Key直接填入UserWithKey对象的Key中，包括id和publicKey
     *
     * @param user 注册信息
     * @return 注册结果
     */
    @Operation(summary = "模拟注册")
    @PostMapping("/register/sim")
    public BaseResult<?> simulatingRegister(@RequestBody @Validated UserWithKey user) {
        user.setPassword(rsaServer.encryptByPublicKey(user.getKey().getPublicKey(), user.getPassword()));
        PrintUtils.print(user.getPassword());
        return userBiz.register(user);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private static final class LoginInfoWithKey extends LoginInfo {

        private RsaKey key;

        public void setKey(RsaKey key) {
            this.key = key;
            withKey(key);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private static final class UserWithKey extends User {

        private RsaKey key;

        public void setKey(RsaKey key) {
            this.key = key;
            withKey(key);
        }
    }
}
