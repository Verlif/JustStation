package idea.verlif.juststation.core.base.result.ext;

import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 14:06
 */
@Schema(name = "一般成功信息")
public class OkResult<T> extends BaseResult<T> {

    public OkResult() {
        super(ResultCode.SUCCESS);
    }

    public OkResult(T data) {
        this();
        this.data = data;
    }
}
