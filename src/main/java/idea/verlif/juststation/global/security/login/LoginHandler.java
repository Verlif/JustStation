package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;

/**
 * 用户登录接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 10:10
 */
public interface LoginHandler {

    /**
     * 用户登录认证后
     *
     * @param t 用户登录信息
     */
    <T extends LoginUser<? extends BaseUser>> void loginAfterAuth(T t);

    /**
     * 退出当前用户登录
     *
     * @return 退出结果
     */
    BaseResult<?> logout();

}
