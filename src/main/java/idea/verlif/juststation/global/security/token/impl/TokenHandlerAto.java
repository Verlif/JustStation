package idea.verlif.juststation.global.security.token.impl;

import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.security.login.domain.BaseUser;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.OnlineUserQuery;
import idea.verlif.juststation.global.security.token.TokenConfig;
import idea.verlif.juststation.global.security.token.TokenHandler;
import idea.verlif.juststation.global.util.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/22 10:23
 */
public class TokenHandlerAto implements TokenHandler {

    private final TokenConfig tokenConfig;
    private final CacheHandler cacheHandler;

    public TokenHandlerAto(TokenConfig tokenConfig, CacheHandler cacheHandler) {
        this.tokenConfig = tokenConfig;
        this.cacheHandler = cacheHandler;
    }

    @Override
    public <T extends LoginUser<? extends BaseUser>> String loginUser(T loginUser) {
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
            return cacheHandler.deleteCacheByMatch(userKey) > 0;
        } else {
            return false;
        }
    }

    @Override
    public <T extends LoginUser<? extends BaseUser>> boolean logout(T loginUser) {
        String key = getTokenKey(loginUser.getToken());
        return cacheHandler.deleteCacheObject(key);
    }

    @Override
    public int logoutAll() {
        LoginUser<?> user = SecurityUtils.getLoginUser();
        if (user == null) {
            return 0;
        }
        OnlineUserQuery query = new OnlineUserQuery();
        query.setUserKey(user.getUsername());
        Set<String> tokenSet = getLoginKeyList(query);
        int count = 0;
        for (String token : tokenSet) {
            if (cacheHandler.deleteCacheObject(token)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public <T extends LoginUser<? extends BaseUser>> T getUserByToken(String token) {
        Claims claims = parseToken(token);
        // 解析对应的权限以及用户信息
        String userKey = (String) claims.get(TokenConfig.TOKEN_NAME);
        return cacheHandler.getCacheObject(getTokenKey(userKey));
    }

    @Override
    public <T extends LoginUser<? extends BaseUser>> void refreshUser(T loginUser) {
        String key = getTokenKey(loginUser.getToken());
        if (loginUser.isRemember()) {
            cacheHandler.setCacheObject(key, loginUser, tokenConfig.getRemember(), TimeUnit.MILLISECONDS);
        } else {
            cacheHandler.setCacheObject(key, loginUser, tokenConfig.getExpireTime(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public <T extends OnlineUserQuery> List<LoginUser<? extends BaseUser>> getOnlineUser(T query) {
        Set<String> set = getLoginKeyList(query);
        List<LoginUser<? extends BaseUser>> list = new ArrayList<>();
        for (String s : set) {
            LoginUser<? extends BaseUser> loginUser = cacheHandler.getCacheObject(s);
            if (loginUser != null) {
                list.add(loginUser);
            }
        }
        return list;
    }

    @Override
    public <T extends OnlineUserQuery> Set<String> getLoginKeyList(T query) {
        StringBuilder sb = new StringBuilder();
        if (query.getUserKey() == null) {
            sb.append("*:");
        } else {
            sb.append(query.getUserKey()).append(":");
        }
        if (query.getLoginTag() == null) {
            sb.append("*:");
        } else {
            sb.append(query.getLoginTag().getTag()).append(":");
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
