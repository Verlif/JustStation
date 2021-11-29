package idea.verlif.juststation.global.security.login.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/12 9:25
 */
@Schema(name = "登录标识")
public enum LoginTag {

    /**
     * 本地登录标识
     */
    LOCAL("local"),
    /**
     * 移动端登录
     */
    MOBILE("mobile"),
    /**
     * PC端登录
     */
    PC("pc");

    /**
     * 登录标识
     */
    @Schema(name = "登录标识")
    private final String tag;

    LoginTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * 获取登录标识
     *
     * @param tag 标识名称
     * @return 登录标识元素；当不存在标识名称对应的元素时，返回{@linkplain LoginTag#LOCAL}
     */
    public static LoginTag getTag(String tag) {
        try {
            return LoginTag.valueOf(tag);
        } catch (IllegalArgumentException ignored) {
            return LOCAL;
        }
    }
}
