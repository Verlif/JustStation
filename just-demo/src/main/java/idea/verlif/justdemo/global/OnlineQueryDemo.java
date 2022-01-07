package idea.verlif.justdemo.global;

import idea.verlif.juststation.global.base.domain.Pageable;
import idea.verlif.juststation.global.security.login.domain.LoginTag;
import idea.verlif.juststation.global.security.login.domain.LoginUser;
import idea.verlif.juststation.global.security.token.OnlineQuery;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/7 14:44
 */
public class OnlineQueryDemo extends Pageable<LoginUser> implements OnlineQuery {

    private String username;

    private LoginTag tag;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public LoginTag getTag() {
        return tag;
    }

    public void setTag(LoginTag tag) {
        this.tag = tag;
    }
}
