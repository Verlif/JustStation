package idea.verlif.juststation.core.test.controller;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import idea.verlif.juststation.global.security.permission.Perm;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/11 11:01
 */
@RestController
@RequestMapping("/permission")
@Api(tags = "权限测试")
public class PermissionController {

    /**
     * 拥有角色[role]时可访问
     */
    @Operation(summary = "拥有角色 - user")
    @Perm(hasRole = "user")
    // 等效于 @PreAuthorize("@pd.hasRole('user')")
    @GetMapping("/hasRole")
    public BaseResult<Object> hasRole() {
        return new OkResult<>().msg("拥有角色");
    }

    /**
     * 拥有关键词[key]时可访问
     */
    @Operation(summary = "拥有关键词 - key")
    @Perm(hasKey = "key")
    // 等效于 @PreAuthorize("@pd.hasKey('key')")
    @GetMapping("/hasKey")
    public BaseResult<Object> hasKey() {
        return new OkResult<>().msg("拥有关键词");
    }

    /**
     * 未拥有角色[role]时可访问
     */
    @Operation(summary = "未拥有角色 - user")
    @Perm(noRole = "user")
    // 等效于 @PreAuthorize("@pd.noRole('user')")
    @GetMapping("/noRole")
    public BaseResult<Object> noRole() {
        return new OkResult<>().msg("未拥有角色");
    }

    /**
     * 未拥有关键词[key]时可访问
     */
    @Operation(summary = "未拥有关键词 - key")
    @Perm(noKey = "key")
    // 等效于 @PreAuthorize("@pd.noKey('key')")
    @GetMapping("/noKey")
    public BaseResult<Object> noKey() {
        return new OkResult<>().msg("未拥有关键词");
    }
}
