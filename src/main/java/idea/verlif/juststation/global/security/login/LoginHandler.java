package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginTag;

/**
 * 用户登录接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 10:10
 */
public interface LoginHandler {

    /**
     * 用户登录
     *
     * @param t 用户登录信息
     * @return 登录结果
     */
    <T extends LoginInfo> BaseResult<?> login(T t);

    /**
     * 退出当前用户登录
     *
     * @return 退出结果
     */
    BaseResult<?> logout();

    /**
     * 退出登录
     *
     * @param tag 登录参数
     * @return 退出结果
     */
    BaseResult<?> logout(LoginTag tag);
}
