package idea.verlif.juststation.core.base.ext;

import idea.verlif.juststation.core.base.BaseResult;
import idea.verlif.juststation.core.base.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 14:06
 */
@Schema(name = "一般错误信息")
public class FailResult<T> extends BaseResult<T> {

    public FailResult() {
        super(ResultCode.FAILURE);
    }
}
