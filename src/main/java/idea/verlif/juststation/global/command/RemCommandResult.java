package idea.verlif.juststation.global.command;

import com.fasterxml.jackson.databind.JsonNode;
import idea.verlif.juststation.global.util.MessagesUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 远程指令结果
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/24 14:23
 */
@Data
public class RemCommandResult implements Serializable {

    private CharSequence message;

    private Code code;

    private JsonNode data;

    public static RemCommandResult build(Code code, CharSequence message, JsonNode data) {
        RemCommandResult result = new RemCommandResult();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static RemCommandResult build(Code code, CharSequence message) {
        RemCommandResult result = new RemCommandResult();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static RemCommandResult build(Code code) {
        return build(code, code.desc);
    }

    public RemCommandResult data(JsonNode data) {
        this.data = data;
        return this;
    }

    /**
     * @author Verlif
     * @version 1.0
     * @date 2021/11/18 14:23
     */
    public enum Code {

        /**
         * 指令正常完成
         */
        OK(MessagesUtils.message("command.code.ok")),
        /**
         * 指令执行失败
         */
        FAIL(MessagesUtils.message("command.code.fail")),
        /**
         * 指令出错
         */
        ERROR(MessagesUtils.message("command.code.error")),
        /**
         * 未知指令
         */
        UNKNOWN(MessagesUtils.message("command.code.unknown"));

        private final String desc;

        Code(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

}
