package idea.verlif.juststation.core.base.result;

import idea.verlif.juststation.global.util.MessagesUtils;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:17
 */
public enum ResultCode {

    /**
     * 成功返回码
     */
    SUCCESS(200, MessagesUtils.message("result.ok")),
    /**
     * 失败返回码
     */
    FAILURE(500, MessagesUtils.message("result.fail")),
    /**
     * Token错误
     */
    FAILURE_TOKEN(501, MessagesUtils.message("result.fail.token")),
    /**
     * 权限不足错误
     */
    FAILURE_UNAVAILABLE(504, MessagesUtils.message("result.fail.unavailable")),
    /**
     * 参数错误
     */
    FAILURE_PARAMETER(510, MessagesUtils.message("result.fail.parameter")),
    /**
     * 缺少参数
     */
    FAILURE_PARAMETER_LACK(511, MessagesUtils.message("result.fail.parameter.lack")),
    /**
     * 文件上传失败
     */
    FAILURE_FILE(520, MessagesUtils.message("result.fail.file")),
    /**
     * 文件上传失败
     */
    FAILURE_FILE_UPLOAD(521, MessagesUtils.message("result.fail.file.upload")),
    /**
     * 文件上传失败
     */
    FAILURE_FILE_DOWNLOAD(522, MessagesUtils.message("result.fail.file.download")),
    /**
     * 文件未找到
     */
    FAILURE_FILE_MISSING(523, MessagesUtils.message("result.fail.file.missing")),
    /**
     * 用户密码错误
     */
    FAILURE_LOGIN_FAIL(530, MessagesUtils.message("result.fail.login")),
    /**
     * 用户不存在
     */
    FAILURE_LOGIN_MISSING(531, MessagesUtils.message("result.fail.login.missing")),
    /**
     * 未登录错误
     */
    FAILURE_NOT_LOGIN(532, MessagesUtils.message("result.fail.login.not")),
    /**
     * 服务器粗欧文
     */
    FAILURE_ERROR(599, MessagesUtils.message("error.default"));

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
