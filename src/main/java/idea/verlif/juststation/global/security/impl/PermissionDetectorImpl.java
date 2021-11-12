package idea.verlif.juststation.global.security.impl;

import idea.verlif.juststation.global.security.permission.PermissionDetector;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import idea.verlif.juststation.global.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:29
 */
public class PermissionDetectorImpl implements PermissionDetector {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean hasRole(String role) {
        LoginUser<?> loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        return loginUser.getRoleSet().stream().anyMatch(s -> s.equals(role));
    }

    @Override
    public boolean hasKey(String key) {
        LoginUser<?> loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        return loginUser.getKeySet().stream().anyMatch(s -> s.equals(key));
    }

}
