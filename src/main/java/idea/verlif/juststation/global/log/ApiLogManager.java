package idea.verlif.juststation.global.log;

import idea.verlif.juststation.global.util.PrintUtils;
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
import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/26 14:26
 */
@Aspect
@Component
public class ApiLogManager {

    public final Map<Class<? extends ApiLogHandler>, ApiLogHandler> handlerMap;

    public ApiLogManager(@Autowired ApplicationContext context) {
        handlerMap = new HashMap<>();
        Map<String, ApiLogHandler> map = context.getBeansOfType(ApiLogHandler.class);
        for (ApiLogHandler handler : map.values()) {
            handlerMap.put(handler.getClass(), handler);
        }
    }

    @Around("@within(idea.verlif.juststation.global.log.LogIt) || @annotation(idea.verlif.juststation.global.log.LogIt)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        Method method = ((MethodSignature) sig).getMethod();

        // 检测方法上的注解
        LogIt logIt = method.getAnnotation(LogIt.class);
        if (logIt == null) {
            logIt = method.getDeclaringClass().getAnnotation(LogIt.class);
        }
        ApiLogHandler handler = handlerMap.get(logIt.handler());
        if (handler != null) {
            handler.onLog(method, logIt);
            Object o = joinPoint.proceed();
            handler.onReturn(method, logIt, o);
            return o;
        } else {
            PrintUtils.print(Level.WARNING, method.getName() + " has not be logged - " + logIt.handler().getSimpleName());
            return joinPoint.proceed();
        }
    }
}
