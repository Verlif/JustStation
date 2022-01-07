package idea.verlif.justdemo.core.login.service;

import idea.verlif.justdemo.global.OnlineQueryDemo;
import idea.verlif.juststation.global.base.domain.SimPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.cache.CacheHandler;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.TokenConfig;
import idea.verlif.juststation.global.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/7 15:31
 */
@Service
public class OnlineService {

    @Autowired
    private CacheHandler cacheHandler;

    /**
     * 获取所有登录用户
     *
     * @param query 查询条件
     * @return 查询结果
     */
    public BaseResult<SimPage<LoginUser>> getOnlineUser(OnlineQueryDemo query) {
        Set<String> set = cacheHandler.findKeyByMatch(TokenConfig.TOKEN_NAME + query.getUsername() + ":*");
        List<LoginUser> list = new ArrayList<>();
        for (String key : set) {
            list.add(cacheHandler.get(key));
        }
        return new OkResult<>(PageUtils.page(list, query));
    }

    public BaseResult<?> logoutUser(String username, LoginTag tag) {
        Set<String> set = cacheHandler.findKeyByMatch(TokenConfig.TOKEN_NAME + username + ":" + (tag == null ? "*" : tag.getTag()) + ":*");
        for (String key : set) {
            cacheHandler.remove(key);
        }
        return new OkResult<>();
    }
}
