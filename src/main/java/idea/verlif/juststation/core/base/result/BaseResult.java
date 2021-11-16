package idea.verlif.juststation.core.base.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:17
 */
@Data
@Schema(name = "接口返回格式")
public class BaseResult<T> {

    /**
     * 自定义返回码
     */
    @Schema(name = "自定义返回码")
    protected Integer code;

    /**
     * 返回信息描述
     */
    @Schema(name = "返回信息描述")
    protected String msg;

    /**
     * 返回数据
     */
    @Schema(name = "返回数据")
    protected T data;

    public BaseResult() {}

    public BaseResult(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public BaseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public BaseResult<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public BaseResult<T> appendMsg(String append) {
        this.msg += append;
        return this;
    }

    public BaseResult<T> withParam(String param) {
        this.msg += " - " + param;
        return this;
    }

    public BaseResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public BaseResult<T> code(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        return this;
    }
}
