package idea.verlif.juststation.global.util;

import idea.verlif.juststation.global.security.exception.CustomException;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
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
@Component
public class SecurityUtils {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public SecurityUtils() {
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new CustomException(MessagesUtils.message("error.no_user"));
        }
    }

    /**
     * 获取用户
     **/
    public static <T extends LoginUser> T getLoginUser() {
        try {
            Authentication authentication = getAuthentication();
            if (authentication == null) {
                return null;
            }
            return (T) authentication.getPrincipal();
        } catch (Exception e) {
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
        return ENCODER.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

}
