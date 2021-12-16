package idea.verlif.juststation.global.security.login;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.security.login.domain.LoginInfo;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import lombok.Data;

import java.util.Set;

/**
 * 用户登录接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 10:10
 */
public interface LoginHandler {

    /**
     * 登录过滤
     *
     * @param t   登录信息
     * @param <T> 登录信息泛型
     * @return 登录结果
     */
    <T extends LoginInfo> LoginResult onLogin(T t);

    /**
     * 当已存在相同的登录标志时的回调方法
     *
     * @param tokens 存在的同标志登录Token
     * @return 处理结果。当结果为成功时，继续后续登录流程；反之则结束登录流程并返回处理结果。
     */
    BaseResult<?> loginWithExist(Set<String> tokens);

    /**
     * 用户登录认证后
     *
     * @param t 用户登录信息
     */
    <T extends LoginUser> void loginAfterAuth(T t);

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
