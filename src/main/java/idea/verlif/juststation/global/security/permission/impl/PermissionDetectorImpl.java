package idea.verlif.juststation.global.security.permission.impl;

import idea.verlif.juststation.global.exception.CustomException;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.PermissionDetector;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.ServletUtils;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:29
 */
public class PermissionDetectorImpl implements PermissionDetector {

    private final TokenService tokenService;

    public PermissionDetectorImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean hasRole(String role) {
        LoginUser<?> loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser == null) {
            throw new CustomException(MessagesUtils.message("result.fail.login.not"));
        }
        return loginUser.getRoleSet().stream().anyMatch(s -> s.equals(role));
    }

    @Override
    public boolean hasKey(String key) {
        LoginUser<?> loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser == null) {
            throw new CustomException(MessagesUtils.message("result.fail.login.not"));
        }
        return loginUser.getKeySet().stream().anyMatch(s -> s.equals(key));
    }

}