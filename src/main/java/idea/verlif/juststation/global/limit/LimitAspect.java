package idea.verlif.juststation.global.limit;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/30 9:04
 */
@Aspect
@Component
public class LimitAspect {

    private final HashMap<String, LimitHandler> handlerMap;

    public LimitAspect(@Autowired ApplicationContext context) {
        handlerMap = new HashMap<>();
        Map<String, LimitHandler> map = context.getBeansOfType(LimitHandler.class);
        for (LimitHandler handler : map.values()) {
            String key = handler.getClass().getName();
            handlerMap.put(key, handler);
        }
    }

    @Around("@annotation(idea.verlif.juststation.global.limit.Limit)")
    public Object onLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("only method can use it!");
        }
        MethodSignature signature = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());

        Limit limit = method.getAnnotation(Limit.class);
        if (limit != null) {
            String key = limit.type().getName();
            if (key.length() == 0) {
                key = method.getName();
            }
            LimitHandler handler = handlerMap.get(key);
            if (handler == null) {
                return new FailResult<>("No such LimitHandler - " + limit.type());
            }
            if (handler.arrived(limit.key())) {
                return joinPoint.proceed();
            } else {
                return new BaseResult<>(ResultCode.FAILURE_LIMIT);
            }
        }
        return joinPoint.proceed();
    }
}
