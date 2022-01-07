package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.security.login.domain.LoginUser;
import io.jsonwebtoken.Claims;
import reactor.util.annotation.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Token服务
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/22 10:13
 */
public interface TokenService {

    /**
     * 登录用户记录
     *
     * @param loginUser 登录用户
     * @return Token
     */
    String loginUser(LoginUser loginUser);

    /**
     * 退出登录
     *
     * @param token 需要退出的token
     * @return 是否退出成功
     */
    boolean logout(String token);

    /**
     * 通过Token获取登录用户信息
     *
     * @param token 用户Token
     * @return 登录用户信息
     */
    @Nullable
    LoginUser getUserByToken(String token);

    /**
     * 刷新用户信息
     *
     * @param loginUser 登录用户信息
     */
    void refreshUser(LoginUser loginUser);

    /**
     * 获取所有在线用户Token
     *
     * @param query 在线用户查询条件
     * @return 在线用户Token列表
     */
    Set<String> getLoginKeyList(OnlineQuery query);

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    Claims parseToken(String token);

    /**
     * 从请求中获取Token
     *
     * @param request 请求对象
     * @return Token
     */
    String getTokenFromRequest(HttpServletRequest request);
}
