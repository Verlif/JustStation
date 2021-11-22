package idea.verlif.juststation.global.util;

import idea.verlif.juststation.global.exception.CustomException;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 安全服务工具类
 *
 * @author Verlif
 */
@Component("SecurityUtils")
public class SecurityUtils {

    private static PasswordEncoder encoder;

    public SecurityUtils(
            @Autowired TokenService tokenService,
            @Autowired(required = false) PasswordEncoder passwordEncoder) {
        if (passwordEncoder == null) {
            encoder = new BCryptPasswordEncoder();
        } else {
            encoder = passwordEncoder;
        }
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUser().getUsername();
        } catch (Exception e) {
            throw new CustomException(MessagesUtils.message("error.no_user"));
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser<? extends BaseUser> getLoginUser() {
        try {
            Authentication authentication = getAuthentication();
            if (authentication == null) {
                return null;
            }
            return (LoginUser<? extends BaseUser>) authentication.getPrincipal();
        } catch (Exception e) {
            PrintUtils.print(e);
            throw new CustomException(MessagesUtils.message("error.no_user"));
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return encoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static PasswordEncoder getEncoder() {
        return encoder;
    }
}
