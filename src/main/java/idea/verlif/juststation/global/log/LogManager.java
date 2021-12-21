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
public class LogManager {

    public final Map<Class<? extends LogHandler>, LogHandler> handlerMap;

    public LogManager(@Autowired ApplicationContext context) {
        handlerMap = new HashMap<>();
        Map<String, LogHandler> map = context.getBeansOfType(LogHandler.class);
        for (LogHandler handler : map.values()) {
            handlerMap.put(handler.getClass(), handler);
        }
    }

    @Around("@annotation(idea.verlif.juststation.global.log.LogIt)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("only method can use it!");
        }
        MethodSignature signature = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());

        LogIt logIt = currentMethod.getAnnotation(LogIt.class);
        LogHandler handler = handlerMap.get(logIt.handler());
        if (handler != null) {
            handler.onLog(currentMethod, logIt);
            Object o = joinPoint.proceed();
            handler.onReturn(currentMethod, logIt, o);
            return o;
        } else {
            PrintUtils.print(Level.WARNING, currentMethod.getName() + " has not be logged - " + logIt.handler().getSimpleName());
            return joinPoint.proceed();
        }
    }

    @Component
    public static final class LogHandlerAto implements LogHandler {

        @Override
        public void onLog(Method method, LogIt logIt) {
            PrintUtils.print(logIt.level().getLevel(), method.getName() + " >> " + logIt.message());
        }

        @Override
        public void onReturn(Method method, LogIt logIt, Object o) {
            PrintUtils.print(logIt.level().getLevel(), method.getName() + " return >> " + o);
        }

    }
}
