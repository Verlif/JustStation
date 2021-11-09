package idea.verlif.juststation.core.base;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:17
 */
public enum ResultCode {

    /**
     * 成功返回码
     */
    SUCCESS(200, "访问成功"),
    /**
     * 失败返回码
     */
    FAILURE(500, "访问失败"),
    /**
     * Token错误
     */
    FAILURE_TOKEN(501, "Token错误"),
    /**
     * 未登录错误
     */
    FAILURE_NOT_LOGIN(502, "未登录"),
    /**
     * 权限不足错误
     */
    FAILURE_UNAVAILABLE(504, "权限不足");

    private final Integer code;

    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
