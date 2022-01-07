package idea.verlif.justdemo.global;

import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.OnlineQuery;
import idea.verlif.juststation.global.security.token.TokenConfig;
import idea.verlif.juststation.global.security.token.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/22 10:23
 */
@Component
public class TokenServiceDemo implements TokenService {

    @Autowired
    private TokenConfig tokenConfig;

    @Autowired
    private CacheHandler cacheHandler;

    @Override
    public String loginUser(LoginUser loginUser) {
        // 生成登录随机Code
        loginUser.setCode(UUID.randomUUID().toString());
        // 刷新用户信息
        refreshUser(loginUser);
        // 生成用户Token
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(TokenConfig.TOKEN_NAME, loginUser.getToken());
        return createToken(claims);
    }

    @Override
    public boolean logout(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token.replace(tokenConfig.getDomain(), ""));
            return cacheHandler.removeByMatch(userKey) > 0;
        } else {
            return false;
        }
    }

    public boolean logout(LoginUser loginUser) {
        String key = getTokenKey(loginUser.getToken());
        return cacheHandler.remove(key);
    }

    public int logoutAll(String username) {
        OnlineQueryDemo query = new OnlineQueryDemo();
        query.setUsername(username);
        Set<String> tokenSet = getLoginKeyList(query);
        int count = 0;
        for (String token : tokenSet) {
            if (cacheHandler.remove(token)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public LoginUser getUserByToken(String token) {
        Claims claims = parseToken(token);
        // 解析对应的权限以及用户信息
        String userKey = (String) claims.get(TokenConfig.TOKEN_NAME);
        return cacheHandler.get(getTokenKey(userKey));
    }

    @Override
    public void refreshUser(LoginUser loginUser) {
        String key = getTokenKey(loginUser.getToken());
        if (loginUser.isRemember()) {
            cacheHandler.put(key, loginUser, tokenConfig.getRemember(), TimeUnit.MILLISECONDS);
        } else {
            cacheHandler.put(key, loginUser, tokenConfig.getExpireTime(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public Set<String> getLoginKeyList(OnlineQuery query) {
        StringBuilder sb = new StringBuilder();
        if (query.getUsername() == null) {
            sb.append("*:");
        } else {
            sb.append(query.getUsername()).append(":");
        }
        if (query.getTag() == null) {
            sb.append("*:");
        } else {
            sb.append(query.getTag().getTag()).append(":");
        }
        sb.append("*");
        return cacheHandler.findKeyByMatch(getTokenKey(sb.toString()));
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

    /**
     * 生成Token的Key值
     *
     * @param userKey 用户唯一键值
     * @return 生成的TokenKey
     */
    private String getTokenKey(String userKey) {
        return tokenConfig.getDomain() + userKey;
    }
}
