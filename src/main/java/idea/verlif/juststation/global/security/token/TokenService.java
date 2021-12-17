package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.security.login.domain.LoginUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * token验证处理
 *
 * @author Verlif
 */
@Component
public class TokenService {

    @Autowired
    private TokenConfig tokenConfig;

    @Autowired
    private TokenHandler tokenHandler;

    public TokenService() {
    }

    public String loginUser(LoginUser loginUser) {
        return tokenHandler.loginUser(loginUser);
    }

    public boolean logout(String token) {
        return tokenHandler.logout(token);
    }

    public boolean logout(LoginUser loginUser) {
        return tokenHandler.logout(loginUser);
    }

    public int logoutAll(String username) {
        return tokenHandler.logoutAll(username);
    }

    public LoginUser getUserByToken(String token) {
        return tokenHandler.getUserByToken(token);
    }

    public void refreshUser(LoginUser loginUser) {
        tokenHandler.refreshUser(loginUser);
    }

    public List<LoginUser> getOnlineUser(OnlineUserQuery query) {
        return tokenHandler.getOnlineUser(query);
    }

    public <T extends OnlineUserQuery> Set<String> getLoginKeyList(T query) {
        return tokenHandler.getLoginKeyList(query);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims parseToken(String token) {
        return tokenHandler.parseToken(token);
    }

    /**
     * 从请求中获取Token
     *
     * @param request 请求对象
     * @return Token
     */
    public @Nullable
    String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader(tokenConfig.getHeader());
    }
}
