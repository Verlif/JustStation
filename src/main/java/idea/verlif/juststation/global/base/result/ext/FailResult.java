package idea.verlif.juststation.global.base.result.ext;

import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 14:06
 */
@Schema(name = "一般错误信息")
public class FailResult<T> extends BaseResult<T> {

    private static final FailResult<?> RESULT_FAIL = new FailResult<Object>() {
        @Override
        public void setCode(Integer code) {
        }

        @Override
        public void setData(Object data) {
        }

        @Override
        public void setMsg(String msg) {
        }
    };

    public FailResult() {
        super(ResultCode.FAILURE);
    }

    public FailResult(String msg) {
        this();
        this.msg = msg;
    }

    public FailResult(ResultCode code) {
        super(code);
    }

    /**
     * 获取无数据失败结果，减少新建对象次数。
     *
     * @return 无数据失败结果，无法改变其中的数据
     */
    public static FailResult<?> empty() {
        return RESULT_FAIL;
    }
}
