package idea.verlif.juststation.global.security.permission;

import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.spring.permission.PermData;
import idea.verlif.spring.permission.PermissionHandler;
import idea.verlif.spring.permission.anno.Perm;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/25 17:04
 */
@Component
public class MyPermissionHandler implements PermissionHandler {

    @Override
    public Object onNoPermData() {
        return new FailResult<>(ResultCode.FAILURE_NOT_LOGIN);
    }

    @Override
    public Object onNoPermission(PermData<?> data, Perm perm, Method method) {
        return new FailResult<>(ResultCode.FAILURE_UNAVAILABLE);
    }
}
