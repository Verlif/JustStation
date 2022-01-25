package idea.verlif.justdemo.core.login.controller;

import idea.verlif.justdemo.core.base.biz.UserBiz;
import idea.verlif.justdemo.core.base.domain.User;
import idea.verlif.justdemo.core.login.NoEncryptService;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.log.Login;
import idea.verlif.juststation.global.rsa.RsaKey;
import idea.verlif.juststation.global.rsa.RsaService;
import idea.verlif.juststation.global.security.login.LoginService;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.permission.Perm;
import idea.verlif.juststation.global.validation.group.Insert;
import idea.verlif.spring.logging.api.LogIt;
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

    /**
     * 登录服务
     */
    @Autowired
    private LoginService loginService;

    /**
     * RSA密钥服务
     */
    @Autowired
    private RsaService rsaService;

    /**
     * 用户业务模块
     */
    @Autowired
    private UserBiz userBiz;

    /**
     * 由于此接口开放，所以请使用Limit注解进行访问限制，避免导致生成的僵尸密钥过多。<br/>
     * 请注意测试中的handler包下的NoEncryptServer，并未使用任何加密模式。<br/>
     * 当注入了NoEncryptServer时，获取的Key中的属性值是null，表示密码明文传输即可。
     *
     * @return 密钥Key
     */
    @GetMapping("/puk")
    @Operation(summary = "获取公钥")
    public BaseResult<RsaKey> getPublicKey() {
        return new OkResult<>(rsaService.genRsaKey());
    }

    /**
     * 用户登录。<br/>
     * 当前版本下，{@linkplain LoginService#login(LoginInfo)}会对于密码进行解密操作。
     * 可通过修改源码或是使用{@linkplain NoEncryptService}将加密解密过程忽略。
     *
     * @param loginInfo 用户登录信息
     * @return 登录结果
     */
    @LogIt(type = Login.class, message = "有人登录啦啦啦")
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
        loginInfo.setPassword(rsaService.encryptByPublicKey(loginInfo.getKey().getPublicKey(), loginInfo.getPassword()));
        return loginService.login(loginInfo);
    }

    /**
     * 当前用户登出，让登录的token失效
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    @Operation(summary = "登出")
    @Perm(hasRole = "user")
    public BaseResult<?> logout() {
        return loginService.logout();
    }

    /**
     * 注册新用户。<br/>
     * 请注意，这里使用的{@linkplain UserBiz#register(User)}会解密密码，请将密码通过{@linkplain #getPublicKey()}获取的公钥加密后进行注册。
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
        user.setPassword(rsaService.encryptByPublicKey(user.getKey().getPublicKey(), user.getPassword()));
        return userBiz.register(user);
    }

    /**
     * 用于模拟登录
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    private static final class LoginInfoWithKey extends LoginInfo {

        private RsaKey key;

        public void setKey(RsaKey key) {
            this.key = key;
        }
    }

    /**
     * 用于模拟注册
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    private static final class UserWithKey extends User {

        private RsaKey key;

        public void setKey(RsaKey key) {
            this.key = key;
            setKeyId(key.getId());
        }
    }
}
