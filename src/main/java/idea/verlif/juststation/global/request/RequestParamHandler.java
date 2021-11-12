package idea.verlif.juststation.global.request;

import idea.verlif.juststation.core.base.domain.Checkable;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
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
public class RequestParamHandler {

    @Around("@annotation(idea.verlif.juststation.global.request.Check)")
    public Object dsPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        // 没有参数时跳过检测
        if (joinPoint.getArgs().length == 0) {
            joinPoint.proceed();
        }
        // 获取调用方法
        Signature sig = joinPoint.getSignature();
        MethodSignature signature;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        signature = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());

        // 筛选出需要检测的参数
        Annotation[][] parameterAnnotations = currentMethod.getParameterAnnotations();
        if (parameterAnnotations.length == 0) {
            return joinPoint.proceed();
        }
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof Check) {
                    Check check = (Check) annotation;
                    Object o = joinPoint.getArgs()[i];
                    if (o instanceof Checkable) {
                        String nu = ((Checkable) o).hasNull(check.tag());
                        if (nu != null) {
                            return new BaseResult<>(ResultCode.FAILURE_PARAMETER).msg("缺少必要参数 - " + nu);
                        }
                    }
                    break;
                }
            }
        }
        return joinPoint.proceed();
    }
}
