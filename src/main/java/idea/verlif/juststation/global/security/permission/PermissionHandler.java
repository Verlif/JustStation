package idea.verlif.juststation.global.security.permission;

import idea.verlif.juststation.global.security.exception.CustomException;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.util.MessagesUtils;
import idea.verlif.juststation.global.util.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 接口入参检测
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 10:46
 */
@Aspect
@Component
public class PermissionHandler {

    @Autowired
    private PermissionDetector permissionDetector;

    @Autowired
    private ApplicationContext context;

    public PermissionHandler() {
    }

    @Around("@within(idea.verlif.juststation.global.security.permission.Perm)")
    public Object dsPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        Method method = ((MethodSignature) sig).getMethod();

        // 检测方法上的权限标记
        Perm perm = method.getAnnotation(Perm.class);
        if (perm != null) {
            validatePerm(perm);
        }
        // 检测类上的权限标记
        perm = method.getDeclaringClass().getAnnotation(Perm.class);
        if (perm != null) {
            validatePerm(perm);
        }
        return joinPoint.proceed();
    }

    private void validatePerm(Perm perm) {
        LoginUser user = SecurityUtils.getLoginUser();
        if (user == null) {
            throw new CustomException(MessagesUtils.message("result.fail.login.not"));
        }
        if (perm.hasKey().length() > 0) {
            if (!permissionDetector.hasKey(user, perm.hasKey())) {
                noPermission();
            }
        }
        if (perm.hasRole().length() > 0) {
            if (!permissionDetector.hasRole(user, perm.hasRole())) {
                noPermission();
            }
        }
        if (perm.noRole().length() > 0) {
            if (!permissionDetector.noRole(user, perm.noRole())) {
                noPermission();
            }
        }
        if (perm.noKey().length() > 0) {
            if (!permissionDetector.noKey(user, perm.noKey())) {
                noPermission();
            }
        }
    }

    public void noPermission() {
        throw new AccessDeniedException(MessagesUtils.message("result.fail.unavailable"));
    }
}
