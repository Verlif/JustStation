package idea.verlif.juststation.global.security.permission.impl;

import idea.verlif.juststation.global.exception.CustomException;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.permission.PermissionDetector;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.SecurityUtils;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 12:29
 */
public class PermissionDetectorImpl implements PermissionDetector {

    public PermissionDetectorImpl() {
    }

    @Override
    public boolean hasRole(String role) {
        LoginUser<?> loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new CustomException(MessagesUtils.message("result.fail.login.not"));
        }
        return loginUser.getRoleSet().stream().anyMatch(s -> s.equals(role));
    }

    @Override
    public boolean hasKey(String key) {
        LoginUser<?> loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new CustomException(MessagesUtils.message("result.fail.login.not"));
        }
        return loginUser.getKeySet().stream().anyMatch(s -> s.equals(key));
    }

}
