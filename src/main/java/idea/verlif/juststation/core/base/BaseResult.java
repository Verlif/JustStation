package idea.verlif.juststation.core.base;

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
    private Integer code;

    /**
     * 返回信息描述
     */
    @Schema(name = "返回信息描述")
    private String msg;

    /**
     * 返回数据
     */
    @Schema(name = "返回数据")
    private T data;

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
