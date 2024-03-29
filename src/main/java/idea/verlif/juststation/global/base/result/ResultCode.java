package idea.verlif.juststation.global.base.result;

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
    SUCCESS(200, MessagesUtils.get("result.ok")),
    /**
     * 失败返回码
     */
    FAILURE(500, MessagesUtils.get("result.fail")),
    /**
     * Token错误
     */
    FAILURE_TOKEN(501, MessagesUtils.get("result.fail.token")),
    /**
     * 权限不足错误
     */
    FAILURE_UNAVAILABLE(504, MessagesUtils.get("result.fail.unavailable")),
    /**
     * 参数错误
     */
    FAILURE_PARAMETER(510, MessagesUtils.get("result.fail.parameter")),
    /**
     * 缺少参数
     */
    FAILURE_PARAMETER_LACK(511, MessagesUtils.get("result.fail.parameter.lack")),
    /**
     * 文件上传失败
     */
    FAILURE_FILE(520, MessagesUtils.get("result.fail.file")),
    /**
     * 文件上传失败
     */
    FAILURE_FILE_UPLOAD(521, MessagesUtils.get("result.fail.file.upload")),
    /**
     * 文件上传失败
     */
    FAILURE_FILE_DOWNLOAD(522, MessagesUtils.get("result.fail.file.download")),
    /**
     * 文件未找到
     */
    FAILURE_FILE_MISSING(523, MessagesUtils.get("result.fail.file.missing")),
    /**
     * 用户登录失败
     */
    FAILURE_LOGIN_FAIL(530, MessagesUtils.get("result.fail.login")),
    /**
     * 用户不存在
     */
    FAILURE_LOGIN_MISSING(531, MessagesUtils.get("result.fail.login.missing")),
    /**
     * 未登录错误
     */
    FAILURE_NOT_LOGIN(532, MessagesUtils.get("result.fail.login.not")),
    /**
     * 访问受限
     */
    FAILURE_LIMIT(533, MessagesUtils.get("result.fail.limit")),
    /**
     * 没有相关数据
     */
    FAILURE_DATA_MISSING(540, MessagesUtils.get("request.data.missing")),
    /**
     * 添加失败
     */
    FAILURE_INSERT(550, MessagesUtils.get("request.insert")),
    /**
     * 更新失败
     */
    FAILURE_UPDATE(560, MessagesUtils.get("request.update")),
    /**
     * 删除失败
     */
    FAILURE_DELETE(570, MessagesUtils.get("request.delete")),
    /**
     * 服务器错误
     */
    FAILURE_ERROR(999, MessagesUtils.get("error.default"));

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
