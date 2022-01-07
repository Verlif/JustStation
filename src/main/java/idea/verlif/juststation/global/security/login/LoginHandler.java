package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import lombok.Data;

/**
 * 用户登录接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 10:10
 */
public interface LoginHandler {

    /**
     * 登录预处理
     *
     * @param t 登录信息
     * @return 登录结果
     */
    default LoginResult preLogin(LoginInfo t) {
        return LoginResult.allowed();
    }

    /**
     * 用户登录认证后返回数据
     *
     * @param t 用户登录信息
     * @return 用户登录认证成功后，返回给前端的数据；为null时则使用默认返回成功数据。
     */
    BaseResult<?> authSuccess(LoginUser t);

    /**
     * 退出当前用户登录
     *
     * @return 退出结果
     */
    BaseResult<?> logout();

    @Data
    class LoginResult {

        /**
         * 是否允许登录
         */
        boolean allow;

        /**
         * 拒接登录时的提示
         */
        String deniedMessage;

        /**
         * 允许登录
         */
        public static LoginResult allowed() {
            LoginResult result = new LoginResult();
            result.setAllow(true);
            return result;
        }

        /**
         * 拒接登录
         *
         * @param message 拒接信息
         */
        public static LoginResult denied(String message) {
            LoginResult result = new LoginResult();
            result.setAllow(false);
            result.setDeniedMessage(message);
            return result;
        }
    }
}
