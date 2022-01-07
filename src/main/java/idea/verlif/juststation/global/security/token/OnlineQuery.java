package idea.verlif.juststation.global.security.token;

import idea.verlif.juststation.global.security.login.domain.LoginTag;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/22 10:20
 */
@Schema(name = "在线用户信息查询条件")
public interface OnlineQuery {

    /**
     * 通过用户名查询
     */
    String getUsername();

    /**
     * 通过登录设备查询
     */
    LoginTag getTag();
}
