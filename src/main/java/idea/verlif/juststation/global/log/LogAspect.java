package idea.verlif.juststation.global.log;

import idea.verlif.juststation.global.util.PrintUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/26 14:26
 */
@Aspect
@Component
public class LogAspect {

    public LogHandler logHandler;

    public LogAspect(@Autowired(required = false) LogHandler logHandler) {
        if (logHandler == null) {
            this.logHandler = new LogHandlerAto();
        } else {
            this.logHandler = logHandler;
        }
    }

    @Around("@annotation(idea.verlif.juststation.global.log.LogIt)")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("only method can use it!");
        }
        MethodSignature signature = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());

        LogIt logIt = currentMethod.getAnnotation(LogIt.class);
        logHandler.onLog(currentMethod, logIt);
        Object o = joinPoint.proceed();
        logHandler.onReturn(currentMethod, logIt, o);
    }

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
