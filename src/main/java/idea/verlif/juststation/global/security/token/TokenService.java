package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.config.CacheHandler;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService {

    @Resource
    private TokenConfig tokenConfig;

    @Resource
    private CacheHandler cacheHandler;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser<?> getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(TokenConfig.TOKEN_NAME);
            String userKey = getTokenKey(uuid);
            return cacheHandler.getCacheObject(userKey);
        }
        return null;
    }

    /**
     * 设置用户登录信息
     */
    public void setLoginUser(LoginUser<?> loginUser) {
        if (loginUser != null && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户登录信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            cacheHandler.deleteCacheObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser<?> loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>(2);
        claims.put(TokenConfig.TOKEN_NAME, token);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     */
    public void verifyToken(LoginUser<?> loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= tokenConfig.getExpireTime()) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser<?> loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + tokenConfig.getExpireTime());
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        cacheHandler.setCacheObject(userKey, loginUser, tokenConfig.getExpireTime().intValue(), TimeUnit.MILLISECONDS);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, tokenConfig.getSecret()).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
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

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request 请求对象
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        return request.getHeader(tokenConfig.getHeader());
    }

    private String getTokenKey(String uuid) {
        return TokenConfig.TOKEN_NAME + ":" + uuid;
    }
}
