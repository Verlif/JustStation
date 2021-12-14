package idea.verlif.juststation.global.exception;

import idea.verlif.juststation.global.base.result.BaseResult;

/**
 * 异常处理类
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/14 15:43
 */
public interface ExceptionHolder<T extends Throwable> {

    /**
     * 注册的异常类型
     *
     * @return 异常类
     */
    Class<? extends T> register();

    /**
     * 异常处理
     *
     * @param e 异常类
     * @return 前端返回结果
     */
    BaseResult<?> handler(T e);
}
