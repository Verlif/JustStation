package idea.verlif.juststation.global.security.permission;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import idea.verlif.juststation.global.security.permission.impl.PermissionDetectorImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final PermissionDetector permissionDetector;

    public PermissionHandler(
            @Autowired(required = false) PermissionDetector permissionDetector) {
        if (permissionDetector == null) {
            this.permissionDetector = new PermissionDetectorImpl();
        } else {
            this.permissionDetector = permissionDetector;
        }
    }

    @Around("@annotation(idea.verlif.juststation.global.security.permission.Perm)")
    public Object dsPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("only method can use it!");
        }
        MethodSignature signature = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());

        Perm perm = currentMethod.getAnnotation(Perm.class);
        if (perm != null) {
            if (perm.hasKey().length() > 0) {
                if (!permissionDetector.hasKey(perm.hasKey())) {
                    return noPermission();
                }
            }
            if (perm.hasRole().length() > 0) {
                if (!permissionDetector.hasRole(perm.hasRole())) {
                    return noPermission();
                }
            }
            if (perm.noRole().length() > 0) {
                if (!permissionDetector.noRole(perm.noRole())) {
                    return noPermission();
                }
            }
            if (perm.noKey().length() > 0) {
                if (!permissionDetector.noKey(perm.noKey())) {
                    return noPermission();
                }
            }
        }
        return joinPoint.proceed();
    }

    public BaseResult<?> noPermission() {
        return new BaseResult<>(ResultCode.FAILURE_UNAVAILABLE);
    }
}
