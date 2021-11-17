package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.cache.redis.RedisCache;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    private final CacheHandler cacheHandler;

    public TokenService(
            @Autowired(required = false) CacheHandler cacheHandler,
            @Autowired RedisTemplate<?, ?> redisTemplate) {
        if (cacheHandler == null) {
            this.cacheHandler = new RedisCache(redisTemplate);
        } else {
            this.cacheHandler = cacheHandler;
        }
    }

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
     * 通过Token获取登录用户信息
     *
     * @param token 用户Token
     * @return 登录用户信息
     */
    public LoginUser<? extends BaseUser> getUserByToken(String token) {
        return cacheHandler.getCacheObject(token);
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
            cacheHandler.deleteCacheByMatch(userKey);
        }
    }

    /**
     * 创建用户令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser<?> loginUser) {
        loginUser.setCode(UUID.randomUUID().toString());
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>(2);
        claims.put(TokenConfig.TOKEN_NAME, loginUser.getToken());
        return createToken(claims);
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
     * 获取所有的在线用户信息
     *
     * @return 在线用户信息集合
     */
    public Set<String> getOnlineTokenList() {
        return cacheHandler.findKeyByMatch(getTokenKey("*"));
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
        return tokenConfig.getDomain() + uuid;
    }
}
