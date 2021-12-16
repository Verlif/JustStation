package idea.verlif.juststation.global.security.permission.impl;

import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.PermissionDetector;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:29
 */
public class PermissionDetectorImpl implements PermissionDetector {

    public PermissionDetectorImpl() {
    }

    @Override
    public boolean hasRole(LoginUser user, String role) {
        return user.getRoleSet().stream().anyMatch(s -> s.equals(role));
    }

    @Override
    public boolean hasKey(LoginUser user, String key) {
        return user.getKeySet().stream().anyMatch(s -> s.equals(key));
    }

}
