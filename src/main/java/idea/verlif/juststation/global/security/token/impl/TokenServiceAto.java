package idea.verlif.juststation.global.security.token.impl;

import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.OnlineQuery;
import idea.verlif.juststation.global.security.token.TokenConfig;
import idea.verlif.juststation.global.security.token.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/7 14:57
 */
public class TokenServiceAto implements TokenService {

    private final CacheHandler cacheHandler;
    private final TokenConfig tokenConfig;

    public TokenServiceAto(CacheHandler cacheHandler, TokenConfig tokenConfig) {
        this.cacheHandler = cacheHandler;
        this.tokenConfig = tokenConfig;
    }

    @Override
    public String loginUser(LoginUser loginUser) {
        // 生成登录随机Code
        loginUser.setCode(UUID.randomUUID().toString());
        String id = loginUser.getToken();
        cacheHandler.put(getCacheKey(id), loginUser, tokenConfig.getExpireTime(), TimeUnit.MILLISECONDS);
        return id;
    }

    @Override
    public boolean logout(String token) {
        return cacheHandler.remove(token);
    }

    @Override
    public LoginUser getUserByToken(String token) {
        return cacheHandler.get(token);
    }

    @Override
    public void refreshUser(LoginUser loginUser) {
        String id = loginUser.getToken();
        cacheHandler.expire(getCacheKey(id), tokenConfig.getExpireTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Set<String> getLoginKeyList(OnlineQuery query) {
        return cacheHandler.findKeyByMatch(getCacheKey("*"));
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    @Override
    public Claims parseToken(String token) {
        Claims body;
        try {
            body = Jwts.parser()
                    .setSigningKey(tokenConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return body;
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader(tokenConfig.getHeader());
    }

    private String getCacheKey(String id) {
        return "station:token:" + id;
    }
}
